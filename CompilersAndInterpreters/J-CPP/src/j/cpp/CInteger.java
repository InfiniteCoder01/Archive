package j.cpp;

class CInteger implements CValue, CValueOperator
	{
	private int value;
	private int seeFrom;
	private int seeTo;
	private boolean seeGlobal;
	private boolean hasValue;
	private String name;
	private String[] operators = {Operator.PLUS,
							   Operator.MINUS,
							   Operator.RAZY, 
							   Operator.DIV, 
							   Operator.MNIEJ_THE_SAME,
							   Operator.WIECEJ_THE_SAME,
							   Operator.MNIEJ, 
							   Operator.WIECEJ, 
							   Operator.TOTAL,
							   Operator.OTHER,
							   Operator.THE_SAME};
	public CInteger()
		{
		// just 4 isOperatorOk;
		}
	public CInteger(String newName)
		{
		name = newName;
		}								   
	public CInteger(String newName, int newValue)
		{
		value = newValue;
		name = newName;
		}	
	public CInteger(String newName, int newValue, int newSeeFrom, int newSeeTo)
		{
		value = newValue;
		seeFrom = newSeeFrom;
		seeTo = newSeeTo;
		name = newName;
		}
	public CInteger(String newName, int newValue, boolean newSeeGlobal)
		{
		value = newValue;
		seeGlobal = newSeeGlobal;
		name = newName;
		}
	public boolean isOperatorOk(String operator)
		{
		for (int i=0;i<operators.length;i++)
			{
			if (operators[i].equals(operator)) return true;
			}
		return false;
		}
	public float getValue()
		{
		return value;
		}
	public void setValue(float newValue)
		{
		value = (int)newValue;
		}
	public int getSeeFrom()
		{
		return seeFrom;
		}
	public void setSeeFrom(int newSeeFrom)
		{
		seeFrom = newSeeFrom;
		}
	public int getSeeTo()
		{
		return seeTo;
		}
	public void setSeeTo(int newSeeTo)
		{
		seeTo = newSeeTo;
		}
	public boolean isGlobal()
		{
		return seeGlobal;
		}
	public void setGlobal(boolean newSeeGlobal)
		{
		seeGlobal = newSeeGlobal;
		}
	public String getName()
		{
		return name;
		}			
	}