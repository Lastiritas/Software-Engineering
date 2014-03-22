package system;

public class Cache 
{	
	public Cache(int size)
	{
		items = new CacheItem[size];
		
		// create all the items here so we don't need to worry about allocations later
		for(int i = 0; i < items.length; i++)
		{
			items[i] = new CacheItem();
		}
	}
	
	public Object tryGet(int id)
	{
		int index = idToIndex(id);
		CacheItem item = items[index];
		
		// no need for a null check because the cache is designed to never 
		// have a null item, check constructor for more information
		
		return item.id == id ? item.data : null;
	}
	
	public void set(int id, Object value)
	{
		int index = idToIndex(id);
		CacheItem item = items[index];
		
		// reuse the old value so we don't waste time with allocations, the cache will 
		// be used often and it needs to avoid GC conflicts
		item.id = id;
		item.data = value;
	}
	
	public void markAsBad(int id)
	{
		int index = idToIndex(id);
		CacheItem item = items[index];
		
		if(item.id == id)
		{
			item.id = -1;
			item.data = null;
		}
	}
	
	private int idToIndex(int id)
	{
		// simple linear hash function because a lot of what we cache will be sequential
		return Math.abs(id) % items.length;
	}
	
	private CacheItem[] items;
	
	private class CacheItem
	{
		public CacheItem()
		{
			id = -1;
			data = null;
		}
		
		public int id;
		public Object data;
	}
}
