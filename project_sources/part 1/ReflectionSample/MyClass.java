class MyClass {
    private Integer count;
    public MyClass(int start) {
        count = start;
    }
    public void increase(int step) {
        count = count + step;
    }
    public String toString(){
    	return count.toString();
    }
}
