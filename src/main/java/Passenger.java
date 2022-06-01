public class Passenger {
    private int passengerId;
    private int survived;
    private int pClass;
    private String name;
    private String sex;
    private int age;
    private int sibSp;
    private int parch;
    private String ticket;
    private float fare;
    private String cabin;
    private char embarked;

    public Passenger(int passengerId, int survived, int pClass, String name, String sex, int age,
                     int sibSp, int parch, String ticket, float fare, String cabin, char embarked) {
        this.passengerId = passengerId;
        this.survived = survived;
        this.pClass = pClass;
        this.name = getFormattedName(name);
        this.sex = sex;
        this.age = age;
        this.sibSp = sibSp;
        this.parch = parch;
        this.ticket = ticket;
        this.fare = fare;
        this.cabin = cabin;
        this.embarked = embarked;
    }
    //formating the name by deleting the Mr. or Mrs.
    private static String getFormattedName(String name) {
        String[] nameArray = name.split(" ");
        String lastName = nameArray[0].substring(0,nameArray[0].length());
        String firstName = "";
        for (int i = 2; i < nameArray.length; i++) {
            firstName = firstName.concat(" " + nameArray[i]);
        }
        String fullName = firstName.concat(" " + lastName);
        return fullName;
    }

    //id range check for filtering
    public boolean passengerInRange(String minRange, String maxRange) {

        try {
            if (maxRange.equals(null) && minRange.equals(null)) {
                return true;
            }
            int min = Constants.MIN;
            int max = Constants.MAX;
            if (!minRange.equals("")) {
                min = Integer.parseInt(minRange);
            }
            if (!maxRange.equals("")) {
                max = Integer.parseInt(maxRange);
            }
            return (this.passengerId <= max && this.passengerId >= min);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //name filter function
    public boolean containsName(String name) {
        try {
            if (name.equals(null)) {
                return true;
            }
            return (this.name.contains(name));
        } catch (Exception e) {
            return false;
        }
    }

    //sibling and spouses filter function
    public boolean sibSPMatch(String num) {
        if(num.equals("")) {
            return true;
        }
        try {
            return this.sibSp == Integer.parseInt(num);
        } catch (Exception e) {
            return false;
        }
    }

    //children and parents filter function
    public boolean parchMatch(String num) {

        if(num.equals("")) {
            return true;
        }
        try {
            return this.parch == Integer.parseInt(num);
        }
        catch(Exception e) {
            return false;
        }
    }

    //ticket number filter function
    public boolean ticketNumMatch(String ticketNum) {
        if(ticketNum.equals("")) {
            return true;
        }
        try {
            return this.ticket.contains(ticketNum);
        } catch (Exception e) {
            return false;
        }

    }

    //ticket fare filter function
    public boolean ticketFareMatch(String minRange, String maxRange) {
        if(minRange.equals("") && maxRange.equals("")) {
            return true;
        }
        try {
            float min = 0;
            float max = Float.MAX_VALUE;
            if(!minRange.equals("")) {
                min = Float.parseFloat(minRange);
            }
            if(!maxRange.equals("")) {
                max = Float.parseFloat(maxRange);
            }
            return this.fare >= min && this.fare <= max;
        } catch (Exception e) {
            return false;
        }
    }

    //cabin filter function
    public boolean cabinMatch(String cabin) {
        try {
            return this.cabin.contains(cabin); //empty string in this case can be a filter due to empty strings present on the table
        } catch (Exception e) {
            return false;
        }
    }

    //class filter function
    public boolean filterClass(String pClass) {
        if (pClass.equals("")) {
            return true;
        }
        switch (pClass) {
            case Constants.ALL:
                return true;
            case Constants.FIRST:
                if (this.pClass == Constants.FIRST_INT) {
                    return true;
                } break;
            case Constants.SECOND:
                if (this.pClass == Constants.SECOND_INT) {
                    return true;
                } break;
            case Constants.THIRD:
                if (this.pClass == Constants.THIRD_INT) {
                    return true;
                } break;
        } return false;
    }

    //sex filter function
    public boolean filterSex(String sex) {

        if(sex.equals("All")) {
            return true;
        }
        try {
            return this.sex.equals(sex);
        } catch (Exception e) {
            return false;
        }
    }

    //embark location filter function
    public boolean filterEmbarked(String embarked) {
        if (embarked.equals("")) {
            return true;
        }
        String thisEmbarked = this.embarked + "";
        return embarked.equals(Constants.ALL) || embarked.equals(thisEmbarked);
    }

    //CLASSES
    public boolean firstClassSurvived() {
        return this.pClass == Constants.FIRST_INT && this.survived == Constants.SURVIVED;
    }
    public boolean secondClassSurvived() {
        return this.pClass == Constants.SECOND_INT && this.survived == Constants.SURVIVED;
    }
    public boolean thirdClassSurvived() {
        return this.pClass == Constants.THIRD_INT && this.survived == Constants.SURVIVED;
    }

    //SEX
    public boolean maleSurvived() {
        return this.sex.equals(Constants.MALE) && this.survived == Constants.SURVIVED;
    }
    public boolean femaleSurvived() {
        return this.sex.equals(Constants.FEMALE) && this.survived == Constants.SURVIVED;
    }

    //AGES
    public boolean ages0to10() {
        return (this.age <= Constants.AGE10 && this.age >= Constants.EMPTY) && this.survived == Constants.SURVIVED;
    }
    public boolean ages11to20() {
        return (this.age <= Constants.AGE20 && this.age >= Constants.AGE11) && this.survived == Constants.SURVIVED;
    }
    public boolean ages21to30() {
        return (this.age <= Constants.AGE30 && this.age >= Constants.AGE21) && this.survived == Constants.SURVIVED;
    }
    public boolean ages31to40() {
        return (this.age <= Constants.AGE40 && this.age >= Constants.AGE31) && this.survived == Constants.SURVIVED;
    }
    public boolean ages41to50() {
        return (this.age <= Constants.AGE50 && this.age >= Constants.AGE41) && this.survived == Constants.SURVIVED;
    }
    public boolean ages51above() {
        return (this.age >= Constants.AGE51) && this.survived == Constants.SURVIVED;
    }

    //FAMILY
    public boolean familySurvived() {
        return (this.sibSp + this.parch > 0) && this.survived == Constants.SURVIVED;
    }
    public boolean noFamilySurvived() {
        return (this.sibSp + this.parch == 0) && this.survived == Constants.SURVIVED;
    }

    //PRICE
    public boolean priceLessThan10() {
        return (this.fare < 10) && this.survived == Constants.SURVIVED;
    }
    public boolean price11to30() {
        return (this.fare >= 11 && this.fare <= 30) && this.survived == Constants.SURVIVED;
    }
    public boolean priceAbove30() {
        return (this.fare > 30) && this.survived == Constants.SURVIVED;
    }

    //EMBARKED
    public boolean embarkedC() {
        return this.embarked == Constants.C && this.survived == Constants.SURVIVED;
    }
    public boolean embarkedQ() {
        return this.embarked == Constants.Q && this.survived == Constants.SURVIVED;
    }
    public boolean embarkedS() {
        return this.embarked == Constants.S && this.survived == Constants.SURVIVED;
    }


    //GETTERS
    public int getSurvived() {
        return survived;
    }
    public int getpClass() {
        return pClass;
    }
    public String getSex() {
        return sex;
    }
    public int getAge() {
        return age;
    }
    public int getSibSp() {
        return sibSp;
    }
    public int getParch() {
        return parch;
    }
    public float getFare() {
        return fare;
    }
    public char getEmbarked() {
        return embarked;
    }

    @Override
    public String toString() {
        return this.passengerId + "," + this.survived + "," + this.pClass + "," + this.name + "," + this.sex + "," + this.age + "," +
                this.sibSp + "," + this.parch + "," + this.ticket + "," + this.fare + "," + this.cabin + "," + this.embarked;
    }
}