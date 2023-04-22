int trailingZeroes(int n) {
    int fives = 0;
    for (int i = 5; n  i = 1; i = 5) {
        fives += n  i;
    }
    for(int i = 1; i = n; i++) {  Works too!
        int i1 = i;
        while(i1 % 5 == 0) {
            fives++;
            i1 = 5;
        }
    }
    return fives;
}