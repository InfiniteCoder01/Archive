package j.cpp;

public class Block
	{
	public final static int MAIN = 0;
	public final static int IF = 1;
	public final static int WHILE = 2;
	public final static int FOR = 3;
	private String ruleLine;
	private int startBlock;
	private int stopBlock;
	private int type;
	private int howManyEnds;	//;
	private int howManyStartStop;		//{}	
	public Block(String newRuleLine, int newType, int newStartBlock, int newStopBlock)
		{
		ruleLine = newRuleLine;
		type = newType;
		startBlock = newStartBlock;
		stopBlock = newStopBlock;
		}		
	public String getRuleLine()
		{
		return ruleLine;
		}
	public void setRuleLine(String newRuleLine)
		{
		ruleLine = newRuleLine;
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
	public int getStartBlock()
		{
		return startBlock;
		}
	public void setStartBlock(int newStartBlock)
		{
		startBlock = newStartBlock;
		}
	public int getStopBlock()
		{
		return stopBlock;
		}
	public void setStopBlock(int newStopBlock)
		{
		stopBlock = newStopBlock;
		}		
	public int getType()
		{
		return type;
		}
	public void setType(int newType)
		{
		type = newType;
		}				
	}