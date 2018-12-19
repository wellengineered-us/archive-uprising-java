/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.*;

public final class LeakDetector
{
	private static final ConcurrentMap<UUID, DisposableList<Disposable>> trackedResources = new ConcurrentHashMap<>();

	private static ConcurrentMap<UUID, DisposableList<Disposable>> getTrackedResources()
	{
		return trackedResources;
	}

	public static void __check()
	{
		StringBuilder sb;
		int slots = 0, objs = 0;

		sb = new StringBuilder();
		for (Map.Entry<UUID, DisposableList<Disposable>> trackedResource : getTrackedResources().entrySet())
		{
			if (trackedResource == null)
				continue;

			if (trackedResource.getKey() == null ||
					trackedResource.getValue() == null)
				throw new InvalidOperationException("");

			final UUID __ = trackedResource.getKey();
			final DisposableList<Disposable> disposables = trackedResource.getValue();
			final int size = disposables.size();
			int ezis = 0;

			for (Disposable disposable : disposables)
			{
				if (disposable == null || disposable.isDisposed())
				{
					ezis--;
					continue;
				}

				sb.append(System.lineSeparator());
				sb.append(String.format("\t\t%s", formatObjectInfo(disposable)));
			}

			final int offset = size + ezis;

			if (offset > 0)
			{
				sb.append(System.lineSeparator());
				sb.append(String.format("\t%s ~ %s#", formatUUID(__), offset));

				slots++;
				objs += offset;
			}
		}

		if (sb.length() > 0)
			sb.append(System.lineSeparator());

		__print(formatCallerInfo(), String.format("check!%s", sb.toString()));

		if (slots > 0)
		{
			final String message = String.format("Leak detector tracked resource check FAILED with %s slots having %s leaked disposable objects", slots, objs);
			__print(formatCallerInfo(), message);
			Utils.failFastOnlyWhen(true, "#leak_detector_check");
		}
	}

	public static void __dispose(UUID __, Disposable disposable)
	{
		if (__ == null)
			throw new ArgumentNullException("__");

		if (disposable == null)
			throw new ArgumentNullException("disposable");

		__dispose(getTrackedResources(), formatCallerInfo(), __, disposable);
	}

	private static void __dispose(ConcurrentMap<UUID, DisposableList<Disposable>> target, String caller, UUID __, Disposable disposable)
	{
		DisposableList<Disposable> disposables;

		if (target == null)
			throw new ArgumentNullException("target");

		if (caller == null)
			throw new ArgumentNullException("caller");

		if (__ == null)
			throw new ArgumentNullException("__");

		if (disposable == null)
			throw new ArgumentNullException("disposable");

		disposables = target.getOrDefault(__, new DisposableList<>());

		if (!disposables.contains(disposable))
			throw new InvalidOperationException(String.format("Leak detector does not contain a record of slot '%s | %s'.", formatUUID(__), formatObjectInfo(disposable)));

		disposables.remove(disposable);

		__print(caller, formatOperation("dispose", __, disposable));
	}

	public static UUID __enter()
	{
		final UUID __ = UUID.randomUUID();

		__print(formatCallerInfo(), formatOperation("enter", __));

		return __;
	}

	public static <T> T __leave(UUID __, T value)
	{
		if (__ == null)
			throw new ArgumentNullException("__");

		__leave(formatCallerInfo(), __);

		return value;
	}

	public static void __leave(UUID __)
	{
		if (__ == null)
			throw new ArgumentNullException("__");

		__leave(formatCallerInfo(), __);
	}

	private static void __leave(String caller, UUID __)
	{
		if (__ == null)
			throw new ArgumentNullException("__");

		__print(caller, formatOperation("leave", __));
	}

	private static void __print(String caller, String message)
	{
		System.out.println(String.format("[%s: %s]", caller, message));
	}

	public static void __reset()
	{
		getTrackedResources().clear();
	}

	private static void __slot(ConcurrentMap<UUID, DisposableList<Disposable>> target, String caller, UUID __, Disposable disposable, String message)
	{
		DisposableList<Disposable> disposables;

		if (target == null)
			throw new ArgumentNullException("target");

		if (caller == null)
			throw new ArgumentNullException("caller");

		if (__ == null)
			throw new ArgumentNullException("__");

		if (disposable == null)
			throw new ArgumentNullException("disposable");

		disposables = target.getOrDefault(__, new DisposableList<>());

		if (disposables.contains(disposable))
			throw new InvalidOperationException(__.toString());

		disposables.add(disposable);

		target.putIfAbsent(__, disposables);

		__print(caller, message);
	}

	public static <T extends Disposable> Disposable __using(UUID __, T disposable)
	{
		final String caller = formatCallerInfo();
		final Disposable anonProxyDisposable = new AbstractLifecycle<Exception, Exception>()
		{
			@Override
			protected void create(boolean creating) throws Exception
			{
				// do nothing
			}

			@Override
			protected void dispose(boolean disposing) throws Exception
			{
				if (this.isDisposed())
					return;

				if (disposing)
				{
					if (disposable != null)
						disposable.dispose();

					__dispose(getTrackedResources(), caller, __, disposable);
				}
			}
		};

		__slot(getTrackedResources(), caller, __, disposable, formatOperation("using", __, disposable));

		return anonProxyDisposable;
	}

	public static void __watching(UUID __, Disposable disposable)
	{
		__slot(getTrackedResources(), formatCallerInfo(), __, disposable, formatOperation("watching", __, disposable));
	}

	private static String formatOperation(String operation, UUID __, Disposable disposable)
	{
		if (operation == null)
			throw new ArgumentNullException("operation");

		if (__ == null)
			throw new ArgumentNullException("__");

		if (disposable == null)
			throw new ArgumentNullException("closeable");

		return String.format("%s => %s", formatOperation(operation, __), formatObjectInfo(disposable));
	}

	private static String formatOperation(String operation, UUID __)
	{
		if (operation == null)
			throw new ArgumentNullException("operation");

		if (__ == null)
			throw new ArgumentNullException("__");

		return String.format("%s#%s", operation, formatUUID(__));
	}
}
