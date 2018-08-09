/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

import java.util.*;

public final class DisposableList<TDisposable extends Disposable> extends AbstractLifecycle<Exception, Exception> implements List<TDisposable>
{
	public DisposableList()
	{
		this(new ArrayList<>());
	}

	public DisposableList(List<TDisposable> disposables)
	{
		if (disposables == null)
			throw new ArgumentNullException("disposables");

		this.disposables = disposables;
	}

	private final List<TDisposable> disposables;

	private List<TDisposable> getDisposables()
	{
		return this.disposables;
	}

	@Override
	public boolean isEmpty()
	{
		return this.getDisposables().isEmpty();
	}

	@Override
	public boolean add(TDisposable disposable)
	{
		return this.getDisposables().add(disposable);
	}

	@Override
	public void add(int index, TDisposable element)
	{
		this.getDisposables().add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends TDisposable> c)
	{
		return this.getDisposables().addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends TDisposable> c)
	{
		return this.getDisposables().addAll(index, c);
	}

	@Override
	public void clear()
	{
		this.getDisposables().clear();
	}

	@Override
	public boolean contains(Object o)
	{
		return this.getDisposables().contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return this.getDisposables().contains(c);
	}

	@Override
	protected void create(boolean creating) throws Exception
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
		{
			if (this.getDisposables() != null)
			{
				for (TDisposable disposable : this.getDisposables())
				{
					if (disposable == null)
						continue;

					try (disposable)
					{
						// do nothing
					}
				}

				this.getDisposables().clear();
			}
		}
	}

	@Override
	public TDisposable get(int index)
	{
		return this.getDisposables().get(index);
	}

	@Override
	public int indexOf(Object o)
	{
		return this.getDisposables().indexOf(o);
	}

	@Override
	public Iterator<TDisposable> iterator()
	{
		return this.getDisposables().iterator();
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return this.getDisposables().lastIndexOf(o);
	}

	@Override
	public ListIterator<TDisposable> listIterator()
	{
		return this.getDisposables().listIterator();
	}

	@Override
	public ListIterator<TDisposable> listIterator(int index)
	{
		return this.getDisposables().listIterator(index);
	}

	@Override
	public boolean remove(Object o)
	{
		return this.getDisposables().remove(o);
	}

	@Override
	public TDisposable remove(int index)
	{
		return this.getDisposables().remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return this.getDisposables().removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return this.getDisposables().retainAll(c);
	}

	@Override
	public TDisposable set(int index, TDisposable element)
	{
		return this.getDisposables().set(index, element);
	}

	@Override
	public int size()
	{
		return this.getDisposables().size();
	}

	@Override
	public List<TDisposable> subList(int fromIndex, int toIndex)
	{
		return this.getDisposables().subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray()
	{
		return this.getDisposables().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return this.getDisposables().toArray(a);
	}
}
