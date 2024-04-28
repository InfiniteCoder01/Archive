package j.cpp;

interface CValue
	{
	public float getValue();
	public void setValue(float newValue);
	public boolean isOperatorOk(String operator);
	}