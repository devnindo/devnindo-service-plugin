package io.devnindo;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestMain {


    public static void main(String... args){

        try{

            String actionPkgName = "com.nft.service.actions.module.command";
            String packageRoot = "com.nft.service";
            System.out.println(actionPkgName.substring((packageRoot+".actions.").length()));

        }catch (Exception excp){
            excp.printStackTrace();
        }

      /*  Path path = Paths.get("D:\\test", "src", "main", "java");
        System.out.println(path.toString());
        String[] parts = {"com", "nft", "service"};

        path = Paths.get(path.toString(), parts);
        System.out.println(path.toString());*/

    }
}
