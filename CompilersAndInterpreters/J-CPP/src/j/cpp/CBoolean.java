package j.cpp;

class CBoolean implements CValueOperator
	{
	private boolean value;
	private int seeFrom;
	private int seeTo;
	private boolean seeGlobal;
	private boolean hasValue;
	private String name;
	private String[] operators = {Operator.AND_LOG,
							   Operator.OR_LOG, 
							   Operator.TOTAL,
							   Operator.OTHER,
							   Operator.THE_SAME,
							   Operator.NOT};	
	public CBoolean()
		{
		// just 4 isOperatorOk;
		}	
	public CBoolean(String newName)
		{
		name = newName;
		}								   
	public CBoolean(String newName, boolean newValue)
		{
		value = newValue;
		hasValue = true;
		name = newName;
		}	
	public CBoolean(String newName, boolean newValue, int newSeeFrom, int newSeeTo)
		{
		value = newValue;
		seeFrom = newSeeFrom;
		seeTo = newSeeTo;
		hasValue = true;
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
	public boolean getValue()
		{
		return value;
		}
	public void setValue(boolean newValue)
		{
		value = newValue;
		hasValue = true;
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