package com.dev.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dev.beans.Borehole;
import com.dev.beans.Fossil;
import com.dev.beans.Measurement;
import com.dev.beans.Rock;

@Controller
public class ObservationsController {

	private static final Logger log = LoggerFactory.getLogger(ObservationsController.class);

	public static final String ROOT = "upload-dir";

	private final ResourceLoader resourceLoader;
	
	List<Borehole> boreholes= new ArrayList<Borehole>();
	List<Fossil> fossils= new ArrayList<Fossil>();
	List<Measurement> measurements= new ArrayList<Measurement>();
	List<Rock> rocks= new ArrayList<Rock>();


	@Autowired
	public ObservationsController(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	/*
	 * 
	 * Redirects to home page
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String provideUploadInfo(Model model) throws IOException {

		clearData();
		return "uploadForm";
	}

	
	@RequestMapping(method = RequestMethod.GET, value = "/{filename:.+}")
	@ResponseBody
	public ResponseEntity<?> getFile(@PathVariable String filename) {

		try {
			return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(ROOT, filename).toString()));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	/*
	 * 
	 * Method to upload all the fles 
	 * including data.txt and other image files
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/")
	public String handleFileUpload(@RequestParam("file") MultipartFile[] files,
			Model model) {
		
		
		clearData();
		String fileName = null;
    	if (files != null && files.length >0) {
    		for(int i =0 ;i< files.length; i++){
	            try {
	                fileName = files[i].getOriginalFilename();
	               
                if(fileName.equals("Data.txt")){
                	Files.copy(files[i].getInputStream(), Paths.get(ROOT, files[i].getOriginalFilename()));
                	processFile(files[i],model);
                }else if(fileName.endsWith(".jpg")){
                	 Files.copy(files[i].getInputStream(), Paths.get(ROOT, files[i].getOriginalFilename()));
                	
                }
	              
	                
	            } catch (Exception e) {
	                return "You failed to upload " + fileName + ": " + e.getMessage() +"<br/>";
	            }
    		}
    		
        } else {
            return "Unable to upload. File is empty.";
        }
		
		return "viewResults";
	}

	
	
	/*
	 * 
	 * Process the Data.txt and assign the values to respective objects
	 * 
	 */
	private Model processFile(MultipartFile file, Model model) throws IOException {
		
		Borehole borehole = null;
		Fossil fossil =null;
		Measurement measurement =null;
		Rock rock = null;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		
		for (String line; (line = reader.readLine()) != null;) {
		   log.info(line);
		    
		    String[] st = line.split("\t+");
		    
		    if(st[0].equals("Name")) continue;
		       
		       // Based on the Type of the observation values are assigned to Boreholes
		       if(st[1].equals("Borehole")) {
		        borehole = new Borehole();
		        borehole.setName(st[0]);
		        borehole.setType(st[1]);
		        borehole.setX(Integer.parseInt(st[2]));
		        borehole.setY(Integer.parseInt(st[3]));
		        borehole.setZ(Integer.parseInt(st[4]));
		        borehole.setDate(toDate(st[5]+" "+st[6]));
		        borehole.setRecordedBy(st[7]);
		        borehole.setRecorderEmail(st[8]);
		        borehole.setDrilledDepth(Float.parseFloat(st[9]));
		        
		        boreholes.add(borehole);
		        
		       }else if(st[1].equals("Fossil")){
		    	    fossil = new Fossil();
		    	    fossil.setName(st[0]);
		    	    fossil.setType(st[1]);
		    	    fossil.setX(Integer.parseInt(st[2]));
		    	    fossil.setY(Integer.parseInt(st[3]));
		    	    fossil.setZ(Integer.parseInt(st[4]));
		    	    fossil.setDate(toDate(st[5]+" "+st[6]));
		    	    fossil.setRecordedBy(st[7]);
		    	    fossil.setRecorderEmail(st[8]);
		    	    fossil.setSpecies(st[9]);
		    	    fossil.setImage(st[10]);
		    	    
		    	    fossils.add(fossil);
		    	   
		       }
		       else if(st[1].equals("Measurement")){
		    	   
		    	    measurement = new Measurement();
		    	    measurement.setName(st[0]);
		    	    measurement.setType(st[1]);
		    	    measurement.setX(Integer.parseInt(st[2]));
		    	    measurement.setY(Integer.parseInt(st[3]));
		    	    measurement.setZ(Integer.parseInt(st[4]));
		    	    measurement.setDate(toDate(st[5]+" "+st[6]));
		    	    measurement.setRecordedBy(st[7]);
		    	    measurement.setRecorderEmail(st[8]);
		    	    measurement.setPorosity(st[9]);
		    	    
		    	    measurements.add(measurement);
		       }
		       else if(st[1].equals("Rock")){
		    	    rock = new Rock();
		    	    rock.setName(st[0]);
		    	    rock.setType(st[1]);
		    	    rock.setX(Integer.parseInt(st[2]));
		    	    rock.setY(Integer.parseInt(st[3]));
		    	    rock.setZ(Integer.parseInt(st[4]));
		    	    rock.setDate(toDate(st[5]+" "+st[6]));
		    	    rock.setRecordedBy(st[7]);
		    	    rock.setRecorderEmail(st[8]);
		    	    rock.setRockName(st[9]);
		    	    rock.setImage(st[10]);
		    	    
		    	    rocks.add(rock);		    	   
		       }
		      
		        
		       model.addAttribute("boreholes", boreholes);
		       model.addAttribute("fossils", fossils);
		       model.addAttribute("measurements", measurements);
		       model.addAttribute("rocks", rocks);
		      try{ 
		       Collections.sort(fossils);
		      }catch(Exception e){
		    	  e.printStackTrace();
		      }
		       model.addAttribute("sortedFossils", fossils);
		       
		       
		    }
		
		return model;
		    
		}

	
	
	private Date toDate(String dateStr) {
	
		  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		  Date date = null ;
	        try
	        {
	             date = simpleDateFormat.parse(dateStr);
	        }
	        catch (ParseException ex)
	        {
	            System.out.println("Exception "+ex);
	        }
	        return date;
	}
		
	@RequestMapping(value = "/getTags", method = RequestMethod.GET)
	public @ResponseBody
	List<String> getTags(@RequestParam String tagName) {
		System.out.println(tagName);
		return simulateSearchResult(tagName.toLowerCase());

	}
	
	@RequestMapping(value = "/getObject", method = RequestMethod.GET)
	public @ResponseBody
	Object getObject(@RequestParam String tagName) {
		System.out.println(tagName);
		return fetchObject(tagName.toLowerCase());

	}

	private Object fetchObject(String tagName) {
	
		for (Fossil fossil : fossils) {
			if (fossil.getName().toLowerCase().contains(tagName)) {
				return fossil;
			}
		}
		
		for (Borehole borehole : boreholes) {
			if (borehole.getName().toLowerCase().contains(tagName)) {
				return (borehole);
			}
		}
		
		for (Measurement measurement : measurements) {
			if (measurement.getName().toLowerCase().contains(tagName)) {
				return(measurement);
			}
		}
		
		for (Rock rock : rocks) {
			if (rock.getName().toLowerCase().contains(tagName)) {
				return(rock);
			}
		}
		return null;
	}

	private List<String> simulateSearchResult(String tagName) {

		List<String> result = new ArrayList<String>();
	
		
		for (Fossil fossil : fossils) {
			if (fossil.getName().toLowerCase().contains(tagName)) {
				result.add(fossil.getName());
			}
		}
		
		for (Borehole borehole : boreholes) {
			if (borehole.getName().toLowerCase().contains(tagName)) {
				result.add(borehole.getName());
			}
		}
		
		for (Measurement measurement : measurements) {
			if (measurement.getName().toLowerCase().contains(tagName)) {
				result.add(measurement.getName());
			}
		}
		
		for (Rock rock : rocks) {
			if (rock.getName().toLowerCase().contains(tagName)) {
				result.add(rock.getName());
			}
		}
		
		return result;
	}
	
	
	private void clearData() {

		fossils.clear();
		boreholes.clear();
		measurements.clear();
		rocks.clear();
		
		File file = new File(Paths.get(ROOT).toUri());
		System.out.println(Paths.get(ROOT).toUri());
        File[] files = file.listFiles(); 
        System.out.println(files.length);
        for (File f:files) 
        {if (f.isFile() && f.exists() && !f.getName().toLowerCase().endsWith(".js")) 
            { f.delete();
System.out.println("successfully deleted");
            }else{
System.out.println("cant delete a file due to open or error");
}
	}
	}
	
	
	
}
