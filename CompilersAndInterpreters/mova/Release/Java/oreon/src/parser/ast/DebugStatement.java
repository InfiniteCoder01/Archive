package parser.ast;

public class DebugStatement implements Statement {

    final Statement text;
    final String textRes;

    public DebugStatement(Statement text) {
        this.text = text;
        textRes = null;
    }

    public DebugStatement(String text) {
        this.text = null;
        textRes = text;
    }

    @Override
    public int getR() {
        System.out.println("Warning: unsuported operation adrGet for debugStatement!");
        return 0;
    }

    @Override
    public void setR(int adrResult) {
        System.out.println("Warning: unsuported operation adrSet for debugStatement!");
    }

    @Override
    public String build() {
        String mod = "";
        if (textRes != null) {
            mod += "c";
        } else {
            mod += "v";
        }
        if (textRes == null) {
            return text.build() + "debug" + mod + " " + text.getR() + "\n";
        }
        return "debug" + mod + " " + textRes + "\n";
    }
}
