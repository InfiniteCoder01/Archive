package emulator;
class Executor {
    void execute(int instr) {
        switch(getGroup(instr)) {
            case 0: 
                int a = 2, b = 3;
                if(getBit(instr, 1) == 1) {
                    b = ~b + 1;
                }
                int c = a + b;
                break;
        }
    }

    private int getGroup(int instr) {
        return 0;
    }

    private int getBit(int n, int i) {
        return (int) (n % Math.pow(2, i + 1) / Math.pow(2, i));
    }
}
