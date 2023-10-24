/*
 * Copyright 2023 devnindo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

      /*  Path path = Paths.get("D:\test", "src", "main", "java");
        System.out.println(path.toString());
        String[] parts = {"com", "nft", "service"};

        path = Paths.get(path.toString(), parts);
        System.out.println(path.toString());*/

    }
}
