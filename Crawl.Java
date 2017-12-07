

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.net.URL;
import java.util.ArrayList;

public class Crawl {

    public static void main(String[] args) {
        
        boolean isValidUrl = false;
        //String urlString = new Scanner(System.in).next();
        String urlString = "";
        
        try{
            if(args.length!=1){
                throw new MalformedURLException();
            }
            urlString = args[0];
            URL url = new URL(urlString);
        }
        catch(MalformedURLException ex){
            System.out.println("Wrong URL or Invalid arguments");
            System.out.println("Write your command in the following format: ");
            System.out.println("Java ProgramName http://test.com");
            System.exit(404);
        }
        
        crawler(urlString);
    }
    
    public static void crawler(String startingURL){
        final int LIMITURLCRAWLING = 100;
        
        System.out.println("Started Crawling:");
        ArrayList<String> crawledList = new ArrayList<>();
        ArrayList<String> pendingList = new ArrayList<>();
        
        pendingList.add(startingURL);
        
        while( crawledList.size() <= LIMITURLCRAWLING && !pendingList.isEmpty() ){
            String urlString = pendingList.remove(0);
            if(!crawledList.contains(urlString)){
                crawledList.add(urlString);
                System.out.println("Crawled: " + urlString);
                
                for(String subUrl: getSubURL(urlString)){
                    if(!crawledList.contains(subUrl)){
                        pendingList.add(subUrl);
                    }
                }
            }
        }
        
        System.out.println("Crawling successful.");
    }
    
    public static ArrayList<String> getSubURL(String urlString) {
        
        ArrayList<String> subUrlList = new ArrayList<>();
        
        Scanner read= null;
        try{
            URL url = new URL(urlString);
            read = new Scanner(url.openStream());
            
            int startIndex = 0;
            while(read.hasNext()){
                String line = read.nextLine();
                startIndex = line.indexOf("http:", startIndex);
                while(startIndex > 0){
                    int endIndex = line.indexOf("\"", startIndex);
                    if(endIndex>0){
                        line = line.substring(startIndex, endIndex);
                        
                        //Some Exception handling
                        if( line.contains("\\/\\/")){
                            line = line.replace("\\/\\/", "//");
                        }
                        if( !line.contains("//")){
                            continue;
                        }
                        
                        subUrlList.add(line);
                        startIndex = line.indexOf("http:", endIndex);
                    }
                    else {
                        startIndex = -1;
                    }

                }

            }
        }catch(MalformedURLException ex){
            System.out.println("Error in converting String to URL");
        }catch(IOException ex){
            System.out.println("Error in reading from URL.");
        }
        
        
        
        return subUrlList;
    }
    
}
