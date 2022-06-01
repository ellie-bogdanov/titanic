import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MainPanel extends JPanel {
    private JComboBox<String> classComboBox;
    private JComboBox<String> sexComboBox;
    private JComboBox<String> embarkedComboBox;
    private String[] valuesOfText;
    private String valueClass;
    private String valueSex;
    private String valueEmbarked;


    public MainPanel (int x, int y, int width, int height) {
        //creating main panel and reaing from titanic excel
        this.setLayout(null);
        this.setBounds(x, y + Constants.MARGIN_FROM_TOP, width, height);

        File file = new File(Constants.PATH_TO_DATA_FILE); //this is the path to the data file
        ArrayList<Passenger> passengerData = new ArrayList<>();
        try {
            if (file.exists()) {
                try (Scanner sc = new Scanner(file)) {
                    if (sc.hasNextLine()) {
                        sc.nextLine();
                    }
                    while (sc.hasNextLine()) {
                        String line = sc.nextLine();
                        passengerData.add(convert(line));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //

        //creating combo boxes for different options
        this.classComboBox = new JComboBox<>(Constants.CLASS_OPTIONS);
        this.classComboBox.setBounds(Constants.LABEL_WIDTH + 1, y,
                Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
        this.add(this.classComboBox);

        this.sexComboBox = new JComboBox<>(Constants.SEX_OPTIONS);
        this.sexComboBox.setBounds(Constants.LABEL_WIDTH + 1, y + Constants.MARGIN_Y,
                Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
        this.add(this.sexComboBox);

        this.embarkedComboBox = new JComboBox<>(Constants.EMBARKED_OPTIONS);
        this.embarkedComboBox.setBounds(Constants.LABEL_WIDTH + 1, this.sexComboBox.getY() + Constants.MARGIN_Y,
                Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
        this.add(this.embarkedComboBox);
        //

        //creating different text fields and storing them into an array so we can use it later
        JTextField[] textFields = new JTextField[Constants.TEXT_FIELDS];
        int textField_y = 3 * Constants.MARGIN_Y;
        for (int i = 0; i < Constants.TEXT_FIELDS; i++) {
            JTextField jTextField = new JTextField();
            jTextField.setBounds(Constants.TEXT_FIELD_X, textField_y, Constants.TEXT_FIELD_WIDTH, Constants.TEXT_FIELD_HEIGHT);
            this.add(jTextField);
            textFields[i] = jTextField;
            textField_y += Constants.MARGIN_Y;
        }
        //

        //same thing as text fields but with labels
        String[] textLabels = {"Passenger Class:", "Passenger Sex:", "Embarked:", "Passenger Num Range, Min:",
                "Passenger Num Range, Max:",
                "Passenger Name:", "Siblings/Spouses:", "Children/Parents:", "Ticket Number:", "Ticket Fare, Min:",
                "Ticket Fare, Max:", "Cabin:"};

        for (int j = 0; j < textLabels.length; j++) {
            JLabel jLabel = new JLabel(textLabels[j]);
            jLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(jLabel);
            y += Constants.MARGIN_Y;
        }
        //

        //adding buttons
        JButton filter = new JButton("Filter");
        filter.setBounds(Constants.FILTER_X, Constants.FILTER_Y, Constants.FILTER_WIDTH, Constants.FILTER_HEIGHT);
        this.add(filter);
        
        JButton statistics = new JButton("create statistics file");
        statistics.setBounds(Constants.STATISTICS_X, Constants.STATISTICS_Y, Constants.STATISTICS_WIDTH, Constants.STATISTICS_HEIGHT);
        Font statFont = new Font("statFont", Font.BOLD, Constants.RESULT_FONT);
        statistics.setFont(statFont);
        this.add(statistics);
        //

        //creating the result label were geting from filter
        JLabel result = new JLabel();
        result.setBounds(Constants.RESULT_X, Constants.RESULT_Y, Constants.RESULT_WIDTH, Constants.RESULT_HEIGHT);
        Font resFont = new Font("resFont", Font.ITALIC, Constants.RESULT_FONT);
        result.setFont(resFont);
        this.add(result);
        //

        
        //adding the values of text fields and combo boxes into an organized array
        filter.addActionListener((e) -> {
            this.valuesOfText = new String[9];
            this.valueClass = this.classComboBox.getSelectedItem().toString();
            this.valueSex = this.sexComboBox.getSelectedItem().toString();
            this.valueEmbarked = this.embarkedComboBox.getSelectedItem().toString();
            for (int i = 0; i < textFields.length; i++) {
                this.valuesOfText[i] = textFields[i].getText();
            }
            List<Passenger> filteredList = filterResult(passengerData);
            List<Passenger> survivedList = filteredList.stream().filter(passenger -> passenger.getSurvived() == Constants.SURVIVED)
                    .collect(Collectors.toList());
            int deceased = filteredList.size() - survivedList.size();

            result.setText("Total rows: " + filteredList.size() + " ( " + survivedList.size() + " survived, " + deceased + " did not )");

            try {
                String nameFile = "src\\main\\resources\\fileNumber.txt";
                String nameNum = readFromFile(nameFile);
                int name = Integer.parseInt(nameNum);
                name++;
                nameNum = name + "";
                writeToFile(nameNum, nameFile);
                String path = new String("src\\main\\resources\\" + nameNum + ".csv");
                String columns = "PassengerId,Survived,Pclass,Name,Sex,Age,SibSp,Parch,Ticket,Fare,Cabin,Embarked";
                try {
                    FileWriter fileWriter = new FileWriter(path);
                    fileWriter.write(columns);
                    for (Passenger passenger : filteredList) {
                        String line = passenger.toString();
                        fileWriter.write(line);
                        fileWriter.write("\n");
                    }
                    fileWriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        //

        //adding event listeners to combo boxes
        this.classComboBox.addActionListener((e) -> {
            this.valueClass = classComboBox.getSelectedItem().toString();
        });
        this.sexComboBox.addActionListener((e) -> {
            this.valueSex = sexComboBox.getSelectedItem().toString();
        });
        this.embarkedComboBox.addActionListener((e) -> {
            this.valueEmbarked = embarkedComboBox.getSelectedItem().toString();
        });
        //

        //calculating survived in specific class and then the total amount
        int firstClass = passengerData.stream().filter(passenger -> passenger.firstClassSurvived()).collect(Collectors.toList()).size();
        int secondClass = passengerData.stream().filter(passenger -> passenger.secondClassSurvived()).collect(Collectors.toList()).size();
        int thirdClass = passengerData.stream().filter(passenger -> passenger.thirdClassSurvived()).collect(Collectors.toList()).size();
        
        int total1stClass = passengerData.stream().filter(passenger -> passenger.getpClass() ==
                Constants.FIRST_INT).collect(Collectors.toList()).size();
        int total2ndClass = passengerData.stream().filter(passenger -> passenger.getpClass() ==
                Constants.SECOND_INT).collect(Collectors.toList()).size();
        int total3rdClass = passengerData.stream().filter(passenger -> passenger.getpClass() ==
                Constants.THIRD_INT).collect(Collectors.toList()).size();
        //

        //calculating survived males and females and the the total amount
        int males = passengerData.stream().filter(passenger -> passenger.maleSurvived()).collect(Collectors.toList()).size();
        int females = passengerData.stream().filter(passenger -> passenger.femaleSurvived()).collect(Collectors.toList()).size();

        int totalMales = passengerData.stream().filter(passenger -> passenger.getSex().equals(Constants.MALE)).
                collect(Collectors.toList()).size();
        int totalFemales = passengerData.stream().filter(passenger -> passenger.getSex().equals(Constants.FEMALE) ).
                collect(Collectors.toList()).size();
        //

        //calculating the amount of survived people in an age range and then the total amount
        int ages0To10 = passengerData.stream().filter(passenger -> passenger.ages0to10()).collect(Collectors.toList()).size();
        int ages11To20 = passengerData.stream().filter(passenger -> passenger.ages11to20()).collect(Collectors.toList()).size();
        int ages21To30 = passengerData.stream().filter(passenger -> passenger.ages21to30()).collect(Collectors.toList()).size();
        int ages31To40 = passengerData.stream().filter(passenger -> passenger.ages31to40()).collect(Collectors.toList()).size();
        int ages41To50 = passengerData.stream().filter(passenger -> passenger.ages41to50()).collect(Collectors.toList()).size();
        int ages51Above = passengerData.stream().filter(passenger -> passenger.ages51above()).collect(Collectors.toList()).size();

        int total0To10 = passengerData.stream().filter(passenger -> (passenger.getAge() <= Constants.AGE10 && passenger.getAge()
                >= Constants.EMPTY)).collect(Collectors.toList()).size();
        int total11To20 = passengerData.stream().filter(passenger -> (passenger.getAge() <= Constants.AGE20 && passenger.getAge()
                >= Constants.AGE11)).collect(Collectors.toList()).size();
        int total21To30 = passengerData.stream().filter(passenger -> (passenger.getAge() <= Constants.AGE30 && passenger.getAge()
                >= Constants.AGE21)).collect(Collectors.toList()).size();
        int total31To40 = passengerData.stream().filter(passenger -> (passenger.getAge() <= Constants.AGE40 && passenger.getAge()
                >= Constants.AGE31)).collect(Collectors.toList()).size();
        int total41To50 = passengerData.stream().filter(passenger -> (passenger.getAge() <= Constants.AGE50 && passenger.getAge()
                >= Constants.AGE41)).collect(Collectors.toList()).size();
        int total51Above = passengerData.stream().filter(passenger -> passenger.getAge() > Constants.AGE51).
                collect(Collectors.toList()).size();
        //

        //calculating the amount of people with at least 1 family member and no family members who survived and then the total amount
        int familySurvived = passengerData.stream().filter(passenger -> passenger.familySurvived()).collect(Collectors.toList()).size();
        int noFamilySurvived = passengerData.stream().filter(passenger -> passenger.noFamilySurvived()).collect(Collectors.toList()).size();

        int totalFamSurvived = passengerData.stream().filter(passenger -> (passenger.getSibSp() + passenger.getParch() > Constants.EMPTY)).
                collect(Collectors.toList()).size();
        int totalNoFamSurvived = passengerData.stream().filter(passenger -> (passenger.getSibSp() + passenger.getParch() == Constants.EMPTY)).
                collect(Collectors.toList()).size();
        // 

        //calculating the amount of people who survived with different ticket price and then the total amount
        int priceLessThan10 = passengerData.stream().filter(passenger -> passenger.priceLessThan10()).collect(Collectors.toList()).size();
        int price11To30 = passengerData.stream().filter(passenger -> passenger.price11to30()).collect(Collectors.toList()).size();
        int priceAbove30 = passengerData.stream().filter(passenger -> passenger.priceAbove30()).collect(Collectors.toList()).size();

        int totalPriceLessThan10 = passengerData.stream().filter(passenger -> passenger.getFare() < Constants.PRICE10).
                collect(Collectors.toList()).size();
        int totalPrice11To30 = passengerData.stream().filter(passenger -> (passenger.getFare() >= Constants.PRICE11 &&
                        passenger.getFare() <= Constants.PRICE30)).
                collect(Collectors.toList()).size();
        int totalPriceAbove30 = passengerData.stream().filter(passenger -> passenger.getFare() > Constants.PRICE30).
                collect(Collectors.toList()).size();
        //

        //calculating the amount of people who embarked from different cities who survived and then the total amount
        int cherbourg = passengerData.stream().filter(passenger -> passenger.embarkedC()).collect(Collectors.toList()).size();
        int queenstown = passengerData.stream().filter(passenger -> passenger.embarkedQ()).collect(Collectors.toList()).size();
        int southampton = passengerData.stream().filter(passenger -> passenger.embarkedS()).collect(Collectors.toList()).size();

        int totalCherbourg = passengerData.stream().filter(passenger -> passenger.getEmbarked() == Constants.C).
                collect(Collectors.toList()).size();
        int totalQueenstown = passengerData.stream().filter(passenger -> passenger.getEmbarked() == Constants.Q).
                collect(Collectors.toList()).size();
        int totalSouthampton = passengerData.stream().filter(passenger -> passenger.getEmbarked() == Constants.S).
                collect(Collectors.toList()).size();
        //

        //PERCENTAGE calculations
        float firstClassP = (float)firstClass*Constants.PERCENTAGES/total1stClass;
        float secondClassP = (float)secondClass*Constants.PERCENTAGES/total2ndClass;
        float thirdClassP = (float)thirdClass*Constants.PERCENTAGES/total3rdClass;
        String[] byClass = {"Survival percentages by class:", "First - " + firstClassP + "%", "Second - " + secondClassP
                + "%", "Third - " + thirdClassP + "%"};

        float malesP = (float)males*Constants.PERCENTAGES/totalMales;
        float femalesP = (float)females*Constants.PERCENTAGES/totalFemales;
        String[] bySex = {"Survival percentages by Sex:", "Males - " + malesP + "%", "Females - " + femalesP + "%"};

        float age0To10P = (float)ages0To10*Constants.PERCENTAGES/total0To10;
        float age11To20P = (float)ages11To20*Constants.PERCENTAGES/total11To20;
        float age21To30P = (float)ages21To30*Constants.PERCENTAGES/total21To30;
        float age31To40P = (float)ages31To40*Constants.PERCENTAGES/total31To40;
        float age41To50P = (float)ages41To50*Constants.PERCENTAGES/total41To50;
        float agesAbove51P = (float)ages51Above*Constants.PERCENTAGES/total51Above;
        String[] byAge = {"Survival percentages by age:", "Ages 0 To 10 - " + age0To10P + "%", "Ages 11 to 20 - " + age11To20P +
                "%", "Ages 21 to 30 - " + age21To30P + "%", "Ages 31 to 40 - " + age31To40P + "%", "Ages 41 to 50 - " +
                age41To50P + "%", "Ages 51 and Above - " + agesAbove51P + "%"};

        float famSurvivedP = (float)familySurvived*Constants.PERCENTAGES/totalFamSurvived;
        float noFamSurvivedP = (float)noFamilySurvived*Constants.PERCENTAGES/totalNoFamSurvived;
        String[] byFamily = {"Survival percentages by family:", "Family Survived - " + famSurvivedP + "%", "No Family Survived - " +
                noFamSurvivedP + "%"};

        float priceLT10P = (float)priceLessThan10*Constants.PERCENTAGES/totalPriceLessThan10;
        float price11To30P = (float)price11To30*Constants.PERCENTAGES/totalPrice11To30;
        float priceAbove30P = (float)priceAbove30*Constants.PERCENTAGES/totalPriceAbove30;
        String[] byPrice = {"Survival percentages by Price:", "Price less than 10 Pounds - " + priceLT10P + "%",
                "Price between 11-30 pounds - " + price11To30P + "%", "Price above 30 pounds - " + priceAbove30P + "%"};

        float cherbourgP = (float)cherbourg*Constants.PERCENTAGES/totalCherbourg;
        float queenstownP = (float)queenstown*Constants.PERCENTAGES/totalQueenstown;
        float southamptonP = (float)southampton*Constants.PERCENTAGES/totalSouthampton;
        String[] byEmbark = {"Survival percentages by Embark:", "Cherbourg - " + cherbourgP + "%", "Queenstown - "
                + queenstownP + "%", "Southampton - " + southamptonP + "%"};
        //

        //adding a label for creating statistics file
        JLabel created = new JLabel("Statistics file created successfully.");
        created.setBounds(Constants.CREATED_X, Constants.CREATED_Y, Constants.CREATED_WIDTH, Constants.CREATED_HEIGHT);
        created.setVisible(false);
        this.add(created);
        //

        //action listener for statistics button in wich we create a statistics file
        statistics.addActionListener((e) -> {
            try {
                String path = "src//main//resources//statistics.txt";
                FileWriter fileWriter = new FileWriter(path);
                for (String text : byClass) {
                    fileWriter.write(text);
                    fileWriter.write("\n");
                }
                for (String text : bySex) {
                    fileWriter.write(text);
                    fileWriter.write("\n");
                }
                for (String text : byAge) {
                    fileWriter.write(text);
                    fileWriter.write("\n");
                }
                for (String text : byFamily) {
                    fileWriter.write(text);
                    fileWriter.write("\n");
                }
                for (String text : byPrice) {
                    fileWriter.write(text);
                    fileWriter.write("\n");
                }
                for (String text : byEmbark) {
                    fileWriter.write(text);
                    fileWriter.write("\n");
                }
                fileWriter.close();

                created.setVisible(true);

            } catch (IOException ex) {
                ex.printStackTrace();
            }


        });
        //
    }


    //filtering to a list using the array wich the fields got stored into
    public List<Passenger> filterResult(ArrayList<Passenger> passengerData) {
        return passengerData.stream().filter(passenger -> passenger.passengerInRange(this.valuesOfText[0], this.valuesOfText[1])).filter(passenger
                -> passenger.containsName(this.valuesOfText[2])).filter(passenger -> passenger.sibSPMatch(this.valuesOfText[3])).filter(passenger ->
                passenger.parchMatch(this.valuesOfText[4])).filter(passenger -> passenger.ticketNumMatch(this.valuesOfText[5])).filter(passenger ->
                passenger.ticketFareMatch(this.valuesOfText[6], this.valuesOfText[7])).filter(passenger ->
                passenger.cabinMatch(this.valuesOfText[8])).filter(passenger -> passenger.filterClass(this.valueClass)).filter(passenger ->
                passenger.filterSex(this.valueSex)).filter(passenger -> passenger.filterEmbarked(this.valueEmbarked)).collect(Collectors.toList());
    }
    //

    //funtion to write content into a file into a file
    public static void writeToFile(String text, String path) {
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(text);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //

    //function to read from a file
    public static String readFromFile(String path) {
        String text = null;
        try {
            if (new File(path).exists()) {
                Scanner scanner = new Scanner(new File(path));
                if (scanner.hasNextLine()) {
                    text = scanner.nextLine();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return text;
    }
    //

    //converting recieved data into a passenger instance
    private static Passenger convert(String data) {
        String[] dataToArr = data.split(",");
        int PassengerId = Integer.parseInt(dataToArr[0]);
        int survived = Integer.parseInt(dataToArr[1]);
        int pClass = Integer.parseInt(dataToArr[2]);
        String name = (dataToArr[3].concat(dataToArr[4]));
        String sex = (dataToArr[5]);
        int age = Constants.EMPTY;
        try {
            age = Integer.parseInt(dataToArr[6]);
        } catch (Exception e) {
        }
        int sibSp = Integer.parseInt(dataToArr[7]);
        int parch = Integer.parseInt(dataToArr[8]);
        String ticket = (dataToArr[9]);
        float fare = Float.parseFloat(dataToArr[10]);
        String cabin = (dataToArr[11]);
        char embarked = ' ';
        try {
            embarked = (dataToArr[12].charAt(0));
        } catch (Exception e) {
        }

        Passenger passenger = new Passenger(PassengerId, survived, pClass, name, sex,
                age, sibSp, parch, ticket, fare, cabin, embarked);

        return passenger;
    }
    //

}