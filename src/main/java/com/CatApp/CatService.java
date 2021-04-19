package com.CatApp;


import com.google.gson.Gson;
import com.squareup.okhttp.*;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class CatService {
    public static void watchCat() throws IOException {
    ///Solicitamos datos
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search").get().build();
        Response response = client.newCall(request).execute();

        String jsonResponse = response.body().string();
        ///Arreglamos el json y lo hacemos imagen
        jsonResponse = jsonResponse.substring(1,jsonResponse.length()-1);
        Gson gson = new Gson();
        Cat cat = gson.fromJson(jsonResponse, Cat.class);
        //Redimensionar la imagen
        Image image = null;
        try {
            URL url = new URL(cat.getUrl());
            image = ImageIO.read(url);
            ImageIcon catImage = new ImageIcon(image);
            if (catImage.getIconWidth() > 800){
                //Redimensionamos
                Image image2 = catImage.getImage();
                Image catImageModified = image2.getScaledInstance(800,600, Image.SCALE_SMOOTH);
                catImage = new ImageIcon(catImageModified);

            }
            String menu = "Opciones: \n"+
                    "1. Ver otra imagen \n"+
                    "2. Fav \n"+
                    "3. Volver \n";
            String [] button = {"Ver otra iamgen", "Favorito", "Volver"};
            String  idCat = cat.getId();
            String option = (String) JOptionPane.showInputDialog(null,
                    menu, idCat, JOptionPane.INFORMATION_MESSAGE,catImage, button, button[0] );

            int selection = -1;

            for(int i =0; i<button.length; i++){
                if (option.equals(button[i])){
                    selection=i;
                }
            }
            switch (selection){
                case 0:
                    watchCat();
                    break;
                case 1:
                    favCat(cat);
                    break;
                default:
                    break;
            }
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public static void  favCat(Cat cat){
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"image_id\":\""+cat.getId()+"\"\n}");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", cat.getApiKey())
                    .build();
            Response response = client.newCall(request).execute();
        }catch (IOException e){

        }
    }

    public static void watchFavorites() throws IOException {
        String apiKey = new Cat().getApiKey();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites")
                .method("GET", null)
                .addHeader("x-api-key", apiKey)
                .addHeader("Content-Type", "text/plain")
                .build();
        Response response = client.newCall(request).execute();

        String jsonResponse = response.body().string();
        Gson gson = new Gson();
        CatFav[] catFavs = gson.fromJson(jsonResponse, CatFav[].class);

        if (catFavs.length>0){
            CatFav catFav = catFavs[0];
            //Redimensionar la imagen
            Image image = null;
            try {
                System.out.println(catFav.getId());
                URL url = new URL(catFav.image.getUrl());
                image = ImageIO.read(url);
                ImageIcon catImage = new ImageIcon(image);
                if (catImage.getIconWidth() > 800){
                    //Redimensionamos
                    Image image2 = catImage.getImage();
                    Image catImageModified = image2.getScaledInstance(800,600, Image.SCALE_SMOOTH);
                    catImage = new ImageIcon(catImageModified);

                }
                String menu = "Opciones: \n"+
                        "1. Ver otra imagen \n"+
                        "2. Eliminar Fav \n"+
                        "3. Volver \n";
                String [] button = {"Ver otra iamgen", "Eliminar Favorito", "Volver"};
                String  idCat = catFav.getId();
                String option = (String) JOptionPane.showInputDialog(null,
                        menu, idCat, JOptionPane.INFORMATION_MESSAGE,catImage, button, button[0] );

                int selection = -1;

                for(int i =0; i<button.length; i++){
                    if (option.equals(button[i])){
                        selection=i;
                    }
                }
                switch (selection){
                    case 0:
                        watchFavorites();
                        break;
                    case 1:
                        deleteFav(catFav);
                        break;
                    default:
                        break;
                }
            }catch (IOException e){
                System.out.println(e);
            }
        }
    }

    public static void deleteFav(CatFav catFav) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"image_id\":\""+catFav.getId()+"\"\n}");
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites/"+catFav.getId()+"")
                .method("DELETE", body)
                .addHeader("x-api-key", catFav.getApiKey())
                .addHeader("Content-Type", "text/plain")
                .build();
        Response response = client.newCall(request).execute();
    }
}
