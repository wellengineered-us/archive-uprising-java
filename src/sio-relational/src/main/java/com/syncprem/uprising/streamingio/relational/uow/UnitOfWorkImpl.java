/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational.uow;

import com.syncprem.uprising.infrastructure.polyfills.*;
import com.syncprem.uprising.streamingio.relational.IsolationLevel;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.Savepoint;
import java.util.Properties;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public class UnitOfWorkImpl extends AbstractLifecycle<Exception, Exception> implements UnitOfWork
{
	public UnitOfWorkImpl(Connection connection, Savepoint savepoint)
	{
		if (connection == null)
			throw new ArgumentNullException("connection");

		this.connection = connection;
		this.savepoint = savepoint;
	}

	private final Connection connection;
	private final Savepoint savepoint;
	private Disposable context;
	private boolean isCompleted;
	private boolean isDiverged;

	@Override
	public Connection getConnection()
	{
		return this.connection;
	}

	@Override
	public Disposable getContext()
	{
		return this.context;
	}

	@Override
	public void setContext(Disposable context)
	{
		this.context = context;
	}

	@Override
	public Savepoint getSavepoint()
	{
		return this.savepoint;
	}

	@Override
	public boolean isCompleted()
	{
		return this.isCompleted;
	}

	private void setCompleted(boolean isCompleted)
	{
		this.isCompleted = isCompleted;
	}

	@Override
	public boolean isDiverged()
	{
		return this.isDiverged;
	}

	private void setDiverged(boolean isDiverged)
	{
		this.isDiverged = isDiverged;
	}

	public static UnitOfWork create(Class<? extends Driver> driverClass, String connectionUrl, boolean transactional, IsolationLevel isolationLevel) throws Exception
	{
		UnitOfWork unitOfWork;
		Connection connection;
		Savepoint savepoint;
		Properties properties;
		final boolean OPEN = true;

		Driver driver;

		if (driverClass == null)
			throw new ArgumentNullException("driverClass");

		if (connectionUrl == null)
			throw new ArgumentNullException("connectionUrl");

		driver = Utils.newObjectFromClass(driverClass);

		if (OPEN)
		{
			properties = new Properties();
			//properties.load(new StringReader(connectionUrl.replace(';','\n')));

			connection = driver.connect(connectionUrl, properties);

			failFastOnlyWhen(connection == null, "connection == null");

			connection.setAutoCommit(!transactional);

			if (transactional)
				savepoint = connection.setSavepoint();
			else
				savepoint = null;
		}

		unitOfWork = new UnitOfWorkImpl(connection, savepoint);

		return unitOfWork;
	}

	private void adjudicate() throws Exception
	{
		try
		{
			if (this.getConnection() != null &&
					this.getSavepoint() != null)
			{
				if (this.isCompleted() && !this.isDiverged())
					this.getConnection().commit();
				else
					this.getConnection().rollback(this.getSavepoint());
			}
		}
		finally
		{
			// destroy and tear-down the context
			if (this.getContext() != null)
				this.getContext().dispose();

			// destroy and tear-down the connection
			if (this.getConnection() != null)
				this.getConnection().close();
		}
	}

	@Override
	public void complete()
	{
		if (this.isDisposed())
			throw new ObjectDisposedException(UnitOfWorkImpl.class.getName());

		if (this.isCompleted())
			throw new InvalidOperationException(String.format("The current unit of work is already complete. You should dispose of the unit of work."));

		this.setCompleted(true);
	}

	@Override
	protected void create(boolean creating)
	{
		if (this.isCreated())
			return;

		if (creating)
		{
			// do nothing
		}
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (this.isDisposed())
			return;

		if (disposing)
			this.adjudicate();
	}

	@Override
	public void divergent()
	{
		if (this.isDisposed())
			throw new ObjectDisposedException(UnitOfWorkImpl.class.getName());

		this.setDiverged(true);
	}
}
