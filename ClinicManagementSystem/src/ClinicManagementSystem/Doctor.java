package ClinicManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
    private Connection connection;


    public Doctor(Connection connection){
        this.connection = connection;

    }


    public  void viewDoctors(){
        String query = "select * from doctor";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Doctors: ");
            System.out.println("+------------+--------------------+--------------------+");
            System.out.println("| Doctor Id  | Doctor Name        | Specialization     |");
            System.out.println("+------------+--------------------+--------------------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                System.out.printf("| %-10s | %-18s | %-18s |\n", id, name, specialization);
                System.out.println("+------------+--------------------+--------------------+");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getDoctorById(int id){
        String query = "select * from doctors where id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
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

