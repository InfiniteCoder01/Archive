#include <iostream>
#include <vector>
#define __max(a, b) (((a) > (b)) ? (a) : (b))

using namespace std;

int repetitions(const vector<char>& dna){
	int max = 0;
	int curr = 0;
	char cOld = 'Z';//Z never happen
	for(char c : dna) {
		if(c == cOld) {
			curr++;
		} else {
			max = __max(max, curr);
			curr = 1;
		}
		cOld = c;
	}
	max = __max(max, curr);
	return max;
}

/*int main() {
	cout << repetitions({'A', 'T', 'T', 'C', 'G', 'G', 'G', 'A'}) << " = 3\n";
	cout << repetitions({'G', 'G', 'G', 'G', 'G', 'G'}) << " = 6\n";
	cout << repetitions({'A', 'A', 'T', 'T', 'C', 'C', 'G', 'G'}) << " = 2\n";
	cout << repetitions({'A', 'C', 'G', 'T', 'A', 'C', 'G', 'T'}) << " = 1\n";
	cout << repetitions({}) << " = 0\n";
	return 0;
}*/