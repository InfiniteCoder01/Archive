package j.cpp;

import java.util.*;
import java.lang.reflect.*;
class Line
	{
	private String line;
	private boolean someInside;
	private List smallLineList;
	private int startStop;			//nawiasy
	private int howManyEnds;	//;
	private int howManyStartStop;		//{}
	private int lineNumber;
	public Line(String newLine)
		{
		line = newLine;
		}
	public int getStartStop()
		{
		return startStop;
		}
	public void addStartStop()
		{
		startStop++;
		}
	public int getHowManyEnds()
		{
		return howManyEnds;
		}		
	public void setHowManyEnds(int newHowManyEnds)
		{
		howManyEnds = newHowManyEnds;
		}
	public int getHowManyStartStop()
		{
		return howManyStartStop;
		}		
	public void setHowManyStartStop(int newHowManyStartStop)
		{
		howManyStartStop = newHowManyStartStop;
		}		
	public int getLineNumber()
		{
		return lineNumber;
		}		
	public void setLineNumber(int newLineNumber)
		{
		lineNumber = newLineNumber;
		}				
	public void removeStartStop()
		{
		startStop--;
		}
	public String getLine()
		{
		return line;
		}
	public boolean isSomeInside()
		{
		return someInside;
		}
	public List getSmallLineList()
		{
		return smallLineList;
		}
	public void setSomeInside(boolean newSomeInside)
		{
		someInside = newSomeInside;
		}
	public void addToSmallLineList(Line newLine)
		{
		if (smallLineList == null) smallLineList = new ArrayList();
		smallLineList.add(newLine);
		}
	public boolean hasInSmallList(Line newLine)
		{
		boolean out = false;
		if (smallLineList != null)
			{
			for (int i=0;i<smallLineList.size();i++)
				{
				if (smallLineList.get(i).equals(newLine)) out = true;
				}
			}
		return out;
		}
	public boolean setInSmallLineList(Line newLine, int number)
		{
		boolean out = false;
		if (smallLineList != null)
			{
			if (number < smallLineList.size() && number > -1)
				{
				smallLineList.set(number, newLine);
				out = true;
				}
			}
		return out;
		}
	public boolean removeFromSmallLineList(int number)
		{
		boolean out = false;
		if (smallLineList != null)
			{
			if (number < smallLineList.size() && number > -1)
				{
				smallLineList.remove(number);
				out = true;
				}
			}
		return out;
		}
	public int getSmallLineListSize()
		{
		int out = -1;
		if (smallLineList != null)
			{
			out = smallLineList.size();
			}
		return out;
		}
	public void clearSmallLineList()
		{
		if (smallLineList != null) smallLineList.clear();
		}
	public static boolean isOutSide(String newLine, String what)
		{
		boolean out = false;
		if (newLine.indexOf(what) != -1)
			{			
			int count = 0;
			for (int l=0;l<newLine.length();l++)
				{
				if (newLine.indexOf("\"", l) == l)
					{
					count++;
					}
				if (newLine.indexOf("\\\"", l) == l)
					{
					count--;
					}
				}
			if ((count % 2) == 0) return true;
			}
		return out;		
		}
	public static boolean isOutSideFor(String newLine, String what)
		{
		boolean out = false;
		if (newLine.indexOf(what) != -1)
			{			
			int count = 0;
			for (int l=0;l<newLine.length();l++)
				{
				if (newLine.indexOf("(", l) == l)
					{
					count++;
					}
				if (newLine.indexOf(")", l) == l)
					{
					count--;
					}
				}
			if ((count % 2) == 0) return true;
			}
		return out;		
		}		
	public void doOneSpace()
		{
		line = line.trim();
		String[] keywords = null;
		String[] operators = null;
		try
			{
			Class c = Class.forName("Keyword");
			Field[] fields = c.getFields();
			Keyword keyword = new Keyword();
			//System.out.println(fields.length);
			keywords = new String[fields.length];
			for (int i=0;i<fields.length;i++)
				{
				keywords[i] = (String)fields[i].get(keyword);
				//System.out.println(fields[i].getName() + " = " + fields[i].get(keyword));
				}
			c = Class.forName("Operator");
			fields = c.getFields();
			Operator operator = new Operator();
			operators = new String[fields.length];
			for (int i=0;i<fields.length;i++)
				{
				operators[i] = (String)fields[i].get(operator);
				}
			}
		catch (ClassNotFoundException e){System.out.println(e);}
		//catch (NoSuchFieldException e){System.out.println(e);}
		catch (IllegalAccessException e){System.out.println(e);}
		for (int i=0;i<line.length();i++)
			{
			boolean wasSome = false;
			for (int j=0;j<keywords.length && !wasSome;j++)
				{
				if (line.indexOf(keywords[j], i) == i)
					{
					int count = 0;
					for (int l=0;l<i;l++)
						{
						if (line.indexOf("\"", l) == l)
							{
							count++;
							//System.out.println(line.substring(l, line.length()));
							//System.out.println(count);
							}
						if (line.indexOf("\\\"", l) == l)
							{
							count--;
							//System.out.println(line.substring(l, line.length()));
							//System.out.println(count);
							}
						}
					if ((count % 2) == 0)
						{
						if (i + keywords[j].length() < line.length())
							{
							while (line.indexOf("  ") == i + keywords[j].length())
								{
								wasSome = true;
								line = line.substring(0, i) + line.substring(i, line.length()).replaceFirst("  ", " ");
								}
							if (wasSome) i = 0;
							for (int k=0;k<operators.length && !wasSome;k++)
								{
								if (!operators[k].equals(" "))	
									{
									if (i + keywords[j].length() + 1 < line.length() && String.valueOf(line.charAt(i + keywords[j].length())).equals(operators[k]))
										{
										line = line.substring(0, i + keywords[j].length()) + " " + line.substring(i + keywords[j].length(), line.length());
										wasSome = true;
										i = 0;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}