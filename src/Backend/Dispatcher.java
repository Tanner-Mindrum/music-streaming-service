package Backend; /**
* The Dispatcher implements DispatcherInterface. 
*
* @author  Oscar Morales-Ponce
* @version 0.15
* @since   02-11-2019 
*/

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.*;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;


public class Dispatcher implements DispatcherInterface {
    HashMap<String, Object> ListOfObjects;
    

    public Dispatcher() throws IOException, org.json.simple.parser.ParseException {
        ListOfObjects = new HashMap<String, Object>();
    }
    
    /* 
    * dispatch: Executes the remote method in the corresponding Object
    * @param request: Request: it is a Json file
    {
        "remoteMethod":"getSongChunk",
        "objectName":"SongServices",
        "param":
          {
              "song":490183,
              "fragment":2
          }
    }
    */
    public String dispatch(String request) throws IOException, ParseException, FileNotFoundException, org.json.simple.parser.ParseException {

        JSONObject jsonReturn = new JSONObject();
        JSONParser parser = new JSONParser();
        JSONObject jsonRequest = (JSONObject) parser.parse(request);

        //System.out.println(jsonRequest);

        try {
            // Obtains the object pointing to SongServices
            Object object = (Object) ListOfObjects.get(jsonRequest.get("objectName"));
            Method[] methods = object.getClass().getMethods();
            Method method = null;
            // Obtains the method
            for (int i=0; i<methods.length; i++)
            {   
                if (methods[i].getName().equals((String) jsonRequest.get("remoteMethod")))
                    method = methods[i];
            }
            if (method == null)
            {
                // jsonReturn.put("error", "Method does not exist");
                jsonReturn.put("error", "Method does not exist");
                return jsonReturn.toString();
            }
            // Prepare the  parameters
            Class[] types =  method.getParameterTypes();
            Object[] parameter = new Object[types.length];
            String[] strParam = new String[types.length];
            Map jsonParam = (JSONObject) jsonRequest.get("param");
            int j = 0;
            Iterator<Map.Entry> releaseItr = jsonParam.entrySet().iterator();
            while (releaseItr.hasNext()) {
                Map.Entry entry = releaseItr.next();
                strParam[j++] = entry.getValue().toString();
            }

            // Prepare parameters
            for (int i=0; i<types.length; i++)
            {
                switch (types[i].getCanonicalName())
                {
                    case "java.lang.Long":
                        parameter[i] =  Long.parseLong(strParam[i]);
                        break;
                    case "java.lang.Integer":
                        parameter[i] =  Integer.parseInt(strParam[i]);
                        break;
                    case "java.lang.String":
                        parameter[i] = new String(strParam[i]);
                        break;
                }
            }
            // Prepare the return
            Class returnType = method.getReturnType();
            String ret = "";
            switch (returnType.getCanonicalName())
            {
                case "java.lang.Long":
                    ret = method.invoke(object, parameter).toString();
                    break;
                case "java.lang.Integer":
                    ret = method.invoke(object, parameter).toString();
                    break;
                case "java.lang.String":
                    ret = (String)method.invoke(object, parameter);
                    break;
                case "java.lang.Boolean":
                    ret = method.invoke(object, parameter).toString();
                    break;
                case "Null":
                    ret = method.invoke(object, parameter).toString();
                    //ret = "Null";
                    break;
            }
            jsonReturn.put("ret", ret);

        } catch (InvocationTargetException | IllegalAccessException e)
        {
            jsonReturn.put("error", "Error on " + jsonRequest.get("objectName").toString() + "." + jsonRequest.get("remoteMethod").toString());
        }

        //System.out.println(jsonReturn.toString());
     
        return jsonReturn.toString();
    }

    /* 
    * registerObject: It register the objects that handle the request
    * @param remoteMethod: It is the name of the method that 
    *  objectName implements. 
    * @objectName: It is the main class that contains the remote methods
    * each object can contain several remote methods
    */
    public void registerObject(Object objectName, String remoteMethod)
    {
        ListOfObjects.put(remoteMethod, objectName);
    }
    
    //////////////////
    /* Just for Testing */
    //////////////////
    public static void main(String[] args) throws IOException, org.json.simple.parser.ParseException {
        // Instance of the Dispatcher
        Dispatcher dispatcher = new Dispatcher();
        // Instance of the services that te dispatcher can handle
        SongDispatcher songDispatcher = new SongDispatcher();
        
        dispatcher.registerObject(songDispatcher, "SongServices");  
    
        // Testing  the dispatcher function
        // First we read the request. In the final implementation the jsonRequest
        // is obtained from the communication module
        try {
            String jsonRequest = new String(Files.readAllBytes(Paths.get("./getSongChunk.json")));
            //String ret = dispatcher.dispatch(jsonRequest);

        } catch (Exception e)
        {
            System.out.println(e);
        }
    } 
}
