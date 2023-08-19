package compiller;
public class ListByte {
    private byte[] a = new byte[0];
    private byte[] b;
    public void add(byte add) {
        b = a;
        a = new byte[b.length + 1];
        System.arraycopy(b, 0, a, 0, b.length);
        a[b.length] = add;
    }
    public long length(){
        return a.length;
    }
    public byte get(int index){
        return a[index];
    }

    public byte[] toArray() {
        return a;
    }

    public void addAll(ListByte list) {
        for (int i = 0; i < list.length(); i++) {
            add(list.get(i));
        }
    }
}
