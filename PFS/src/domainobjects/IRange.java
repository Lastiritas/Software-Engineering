package domainobjects;

public interface IRange<T extends Comparable<T>>  
{
	T getLower();
	T getUpper();
}
