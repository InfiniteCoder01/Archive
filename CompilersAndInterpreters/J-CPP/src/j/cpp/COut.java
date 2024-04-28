package j.cpp;

import java.util.List;
import java.util.ArrayList;
public class COut
	{
	private String[] out;
	private JCppUtil jCppUtil;
	private List myInitList;
	private int myLineNumber;
	public COut(String text, List initList, int lineNumber)
		{
		myInitList = initList;
		myLineNumber= lineNumber;
		jCppUtil = new JCppUtil();
		Init init = null;
		List list = new ArrayList();
	//	System.out.println(text);
		text = text.trim();
		if (Line.isOutSide(text.substring(0, text.indexOf("cout") + "cout".length()), "cout") && text.lastIndexOf(";") > 0)
			{
		//	System.out.println("ok");
			text = text.replaceFirst("cout", " ");
		//	System.out.println(text);
			}
		else throw new JCppBadStatementException("!!!!!!!!!!niew�a�ciwa sk�adnia \"cout\"");
		while (text.indexOf("<<") != -1)
			{
			//text = text.replaceFirst("<<", " ");
			text = text.substring(text.indexOf("<<") + 2, text.length());
			text = text.trim();
		//	System.out.println(text);
			if (text.indexOf("<<") != -1 && Line.isOutSide(text.substring(0, text.indexOf("<<") + "<<".length()), "<<"))
				{
				list.add(this.getStringValue(text.substring(0, text.indexOf("<<")).trim()));
				}
			else list.add(this.getStringValue(text.substring(0, text.lastIndexOf(";")).trim())); 
			}
		if (list.size() > 0) out = new String[list.size()];
		for (int i=0;i<list.size();i++)
			{
			out[i] = (String)list.get(i);
			}
		}
	public String getStringValue(String text)
		{
		String out = text;
		Sentence sentence;
		if (text.indexOf("\"") == 0) out = JCppUtil.getLost(text, "\"");
		else if (text.equals("endl")) out = "\n";
			else
				{
				System.out.println(text + "FROM COUT");
				sentence = new Sentence(text, myInitList, myLineNumber);
				out = sentence.getValueString();
				}
		//System.out.println("**" + out + "**");
		return out;
		}
	public String[] getOut()
		{
		return out;
		}
	public void setOut(String[] newOut)
		{
		out = newOut;
		}
	public static void main(String[] argv)
		{
		Init jeden = new Init("jeden", Type.FLOAT, (float)10);
		jeden.setGlobal(true);
		Init dwa = new Init("dwa", Type.INTEGER, 80);
		dwa.setGlobal(true);
		List list = new java.util.ArrayList();
		list.add(jeden);
		list.add(dwa);			
		COut cOut = new COut(" cout<< endl<<   2 + jedenf << \"sdsdsdsd\" << dwa <<endl;", list, 1);
		System.out.println("Calosc");
		for (int i=0;i<cOut.getOut().length;i++)
			{
			System.out.print(cOut.getOut()[i]);
			}
		}
	}