package com.CatApp;

import javax.swing.*;
import java.io.IOException;

public class Init {
    public static void main(String[] args) throws IOException {
        int optionMenu = -1;
        String [] button = {"1. Ver gatos", "2. Ver favoritos","3. Salir"};
        do{
            String option = (String)JOptionPane
                    .showInputDialog(null, "Gatos Java",
                            "Menu principal", JOptionPane.INFORMATION_MESSAGE,
                            null,button, button[0]);

            for(int i =0; i<button.length; i++){
                if (option.equals(button[i])){
                    optionMenu=i;
                }
            }
            switch (optionMenu){
                case 0:
                    CatService.watchCat();
                    break;
                case 1:
                    CatService.watchFavorites();
                    break;
                default:
                    break;
            }
        }while (optionMenu!=1);

    }
}
