package ClinicManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class ClinicManagementSystem {
    private static final String url = "jdbc:oracle:thin:@localhost:1521/XE";
    private static final String username = "Clinic";
    private static final String password = "Clinic";

    public static void main(String[] args) {
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url,username,password);
            System.out.println("Connection Successful");
            Patient patient = new Patient(connection,scanner);
            Doctor doctor = new Doctor(connection);

            while (true){
                System.out.println("CLINIC MANAGEMENT SYSTEM");
                System.out.println("1. Add patient");
                System.out.println("2. View Patient");
                System.out.println("3. view Doctor");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter Your Choice");
                int choice = scanner.nextInt();



                switch (choice){

                    case 1 -> {scanner.nextLine();
                        patient.addPatient();
                        System.out.println();
                    }
                    case 2 -> {
                        patient.viewPatient();
                        System.out.println();
                    }
                    case 3 -> {
                        doctor.viewDoctors();
                        System.out.println();
                    }
                    case 4 -> {
                        bookAppointment(patient,doctor,connection,scanner);
                        System.out.println();
                    }
                    case 5 -> {
                        return;
                    }
                    default -> System.out.println("Enter Valid Choice");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void bookAppointment( Patient patient , Doctor doctor,Connection connection,Scanner scanner){
        System.out.println("Enter Patient Id");
        int patientId = scanner.nextInt();
        System.out.println("Enter Doctor Id");
        int doctorId = scanner.nextInt();
        System.out.println("Enter appointment date (YYYY-MM-DD)");
        String appointmentDate = scanner.next();
        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(isDoctorAvalible(doctorId,appointmentDate,connection)){
                String appoientmentQuery = "INSERT INTO appointments(patient_id,doctor_id,appointment_date) values (?,?,?,?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appoientmentQuery);
                    Statement seqQuery = connection.createStatement();
                    ResultSet seq = seqQuery.executeQuery("SELECT auto_increment_appointment.nextval from dual");
                    seq.next();
                    preparedStatement.setInt(1,seq.getInt(1));
                    preparedStatement.setInt(2,patientId);
                    preparedStatement.setInt(3,doctorId);
                    preparedStatement.setString(4,appointmentDate);
                    seq.close();
                    int affectedRow = preparedStatement.executeUpdate();
                    if(affectedRow>0){
                        System.out.println("Appointment Booked");
                    }else {
                        System.out.println("Failed to Book Appointment");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("Doctor not available on this date");
            }

        }else {
            System.out.println("Either Doctor Or Patient Doesn't Exist ");
        }
    }

    public static boolean isDoctorAvalible(int doctorId , String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appiontment WHERE doctor_id= ? AND appointment_date = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count == 0){
                    return true;
                }else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
