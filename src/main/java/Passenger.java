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

    public boolean cabinMatch(String cabin) {
        try {
            return this.cabin.contains(cabin); //empty string in this case can be a filter due to empty strings present on the table
        } catch (Exception e) {
            return false;
        }
    }

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

    public boolean filterEmbarked(String embarked) {
        if (embarked.equals("")) {
            return true;
        }
        String thisEmbarked = this.embarked + "";
        return embarked.equals(Constants.ALL) || embarked.equals(thisEmbarked);
    }


    @Override
    public String toString() {
        return "Passenger{" +
                "passengerId=" + passengerId +
                ", survived=" + survived +
                ", pClass=" + pClass +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", sibSp=" + sibSp +
                ", parch=" + parch +
                ", ticket='" + ticket + '\'' +
                ", fare=" + fare +
                ", cabin='" + cabin + '\'' +
                ", embarked=" + embarked +
                '}';
    }
}
