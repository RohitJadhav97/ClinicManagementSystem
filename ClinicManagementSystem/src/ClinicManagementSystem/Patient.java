package ClinicManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection,Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }
    public  void addPatient(){
       // scanner.next();
        System.out.print("Enter patient Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter patient Age: ");
        int age = scanner.nextInt();
        System.out.print("Enter patient Gender: ");
        String gender = scanner.next();

        try{
            String query = "INSERT INTO patient values (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            Statement seq_qurye = connection.createStatement();
            ResultSet seq = seq_qurye.executeQuery("select auto_increment_patient.nextval from dual");
            seq.next();
            preparedStatement.setInt(1,seq.getInt(1));
            preparedStatement.setString(2,name);
            preparedStatement.setInt(3,age);
            preparedStatement.setString(4,gender);
            int affectedRow = preparedStatement.executeUpdate();
            seq.close();
            if(affectedRow>0){
                System.out.println("Patient Added Successfully");
            }
            else {
                System.out.println("Failed To Add Patient");
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public  void viewPatient(){
        String query = "select * from patient";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Patient: ");
            System.out.println("+------------+--------------------+--------+------------+");
            System.out.println("| Patient Id | Patient Name       | Age    | Gender     |");
            System.out.println("+------------+--------------------+--------+------------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("| %-10s | %-18s | %-6s | %-10s |\n", id, name, age, gender);
                System.out.println("+------------+--------------------+--------+------------+");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id){
        String query = "select * from patient where id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

