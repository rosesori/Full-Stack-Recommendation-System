public class Person {
    public String fName;
    public String lName;
    public String number;
    public String pAddress;

    public Person(String fName, String lName, String number, String pAddress)
    {
        this.fName = fName;
        this.lName = lName;
        this.number = number;
        this.pAddress = pAddress;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getpAddress() {
        return pAddress;
    }

    public void setpAddress(String pAddress) {
        this.pAddress = pAddress;
    }

    @Override
    public String toString() {
        String s = "";
        s+=(lName + ", " + fName);
        return s;
    }
}
