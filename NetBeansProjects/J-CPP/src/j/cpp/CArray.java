package j.cpp;

public class CArray
	{
	private int length;
	private int type;
	private boolean[] valueBool;
	private int[] valueInteger;
	private float[] valueFloat;
	public int getLength()
		{
		return length;
		}
	public void setLength(int newLength)
		{
		length = newLength;
		}
	public int getType()
		{
		return type;
		}
	public void setType(int newType)
		{
		type = newType;
		}
	public boolean[] getValueBool()
		{
		return valueBool;
		}
	public void setValueBool(boolean[] newValueBool)
		{
		valueBool = newValueBool;
		}
	public int[] getValueInteger()
		{
		return valueInteger;
		}
	public void setValueInteger(int[] newValueInteger)
		{
		valueInteger = newValueInteger;
		}
	public float[] getValueFloat()
		{
		return valueFloat;
		}
	public void setValueFloat(float[] newValueFloat)
		{
		valueFloat = newValueFloat;
		}			
	}