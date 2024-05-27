package com.bworks.data.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.bworks.util.AppConstants;
import java.util.Base64;

/**
 * FileManager
 * @author Nikhil N K
 *
 */
public class FileManager {

	private List<String> filePath = new ArrayList<String>();
	private String encoding;

	/**
	 * Search for file or directory
	 * @param name
	 * @param file
	 * @param isSearchDirectory
	 * @return
	 */
	public List<String> search(String name, File directory, boolean isSearchDirectory) {	
		searchDirectories(name, directory, isSearchDirectory);
		return filePath;
 	}
	
	/**
	 * Search for file or directory
	 * @param name
	 * @param file
	 * @param isSearchDirectory
	 * @return
	 */
	public List<String> search(String name, String directory, boolean isSearchDirectory) {	
		searchDirectories(name, new File(directory), isSearchDirectory);
		return filePath;
 	}
	
	/**
	 * Search for file or directory
	 * @param name
	 * @param directory
	 * @param isSearchDirectory
	 */
	private void searchDirectories(String name, File directory, boolean isSearchDirectory) {

		File[] list = null;
		
		if(directory !=null && directory.exists() && name != null) {
			list = directory.listFiles();
			if(list != null) {
				for (File file : list) {
				    if (file.isDirectory()) {
				    	if(isSearchDirectory && (name.equals(file.getName()))) {
				    		filePath.add(file.getAbsolutePath() + AppConstants.FILE_SEPARATOR);
				    	}
				        search(name,file, isSearchDirectory);
				    }
				    else if (name.equals(file.getName())) {
				    	filePath.add(file.getAbsolutePath() + AppConstants.FILE_SEPARATOR);
				    }
				}
			}
		}	
	}
	
	/**
	 * Move files or folders
	 * @param source
	 * @param target
	 * @param copyOption
	 * @return
	 * @throws Exception 
	 */
	public boolean move(File source, File target) throws Exception {
		
		boolean status = false;
		
		try {
	           if(source.exists() && source.isDirectory()) {
	 	           target = new File(target, source.getName());
	 	           status = target.mkdirs();
	 	           status = status && source.listFiles().length > 0;
	 	           for(File file : source.listFiles()) {
	 	        	   new File(target, file.getName()).delete();
	 	        	  FileInputStream sourceStream = new FileInputStream(file);
	 	        	   status = status && Files.copy(sourceStream, 
	 	        			   Paths.get(new File(target, file.getName()).getAbsolutePath()),
	 	        			   StandardCopyOption.REPLACE_EXISTING) > 0;
	 	        	   sourceStream.close();
 	        	   }
	        	}
	           if(status) {
	        	   status = this.deleteFolder(source);
	           } else {
	        	   this.deleteFolder(target);
	           }
	           
		} catch (Exception e) {
			status = false;
			throw e;
		}
		
		return status;
	}
	
	/**
	 * Move files or folders
	 * @param source
	 * @param target
	 * @param copyOption
	 * @return
	 * @throws Exception 
	 */
	public boolean move(String source, String target) throws Exception {
		return move(new File(source), new File(target));
	}	
	
	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException 
	 */
	public List<String> readAsLines(String filePath) throws IOException {
		
		List<String> list=new ArrayList<String>();
		BufferedReader bufferedReader = null;
		InputStreamReader streamReader = null;

        try {            
        	String line = null;
        	if(filePath != null && new File(filePath).exists()) {
        		if(encoding != null) {
        			streamReader = new InputStreamReader(new FileInputStream(filePath), encoding);
        		} else {
        			streamReader = new InputStreamReader(new FileInputStream(filePath));
        		}
	        	bufferedReader = new BufferedReader(streamReader);
	            while((line = bufferedReader.readLine()) != null) {             
	            	list.add(line);
	            }
        	}
        }
        catch(IOException e) {
        	throw e;
        }      
        finally {
        	if(bufferedReader != null) {
        		bufferedReader.close();
        	}
        }
        
        return list;
	}
	
	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException 
	 */
	public String readAsText(String filePath) throws IOException {

		String content = null;
		InputStreamReader streamReader = null;
		char[] buffer = null;
		
		try {
			if(filePath != null && new File(filePath).exists()) {
				buffer = new char[(int) new File(filePath).length()];
        		if(encoding != null) {
        			streamReader = new InputStreamReader(new FileInputStream(filePath), this.encoding);
        		} else {
        			streamReader = new InputStreamReader(new FileInputStream(filePath));
        		}
        		streamReader.read(buffer);
        		content = new String(buffer);
        		content = new String(content.getBytes("UTF-8"));
			}
		}
		catch(IOException e) {
    	  	throw e;
      	} finally {
      		if(streamReader != null) {
      			streamReader.close();
      		}
      	}

		return content;		 
	}
	
	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public String readAsBase64(String filePath) throws IOException {
	   return new String(Base64.getEncoder().encode((readAsText(filePath)).getBytes()));
	}
	
	
	
	public static String encodeImage(byte[] imageByteArray) {
        return Base64.getEncoder().encode(imageByteArray).toString();
    }
	
	
	
	
	
	public String readAsImage(String filePath) throws IOException{

		InputStreamReader streamReader = null;
		;
		String image=null;
		String fileFormat="";
		try {
			if(filePath != null && new File(filePath).exists()) {
				File imageFile= new File(filePath);

		          BufferedImage buffImage = ImageIO.read(imageFile);

		    if (buffImage != null) {
		          java.io.ByteArrayOutputStream os = new 
		                      java.io.ByteArrayOutputStream();

		          // Trying to get the image format from file object.
		          fileFormat = getImageFileFormat(imageFile);
		          
		          // If the file format unavailable from the file content then trying to get from the file name.  
		          if(null == fileFormat || fileFormat.isEmpty())
		          {
		        	  fileFormat = getImageFileFormat(filePath);
		          }
		          
		          // This may raise exception if fileFormat is empty or null.
		          ImageIO.write(buffImage, fileFormat, os);
		          byte[] data = os.toByteArray();
		          image = Base64.getEncoder().encode(data).toString();
			}
		}
		}
		catch(IOException e) {
    	  	throw e;
      	} finally {
      		
      		if(streamReader != null) {
      			streamReader.close();
      		}
      	}

		 return image;
	}
	
	/**
	 * 
	 * @param imageFile
	 * @return
	 * @throws IOException
	 */
	private String getImageFileFormat(File imageFile) throws IOException
	{
		ImageInputStream imgInStream = null;
		Iterator<?>      iter        = null; 
		String           format      = "";
		ImageReader      reader      = null; 
		boolean          bOK         = false;
		
		// Trying to get the image format from image file.
		try
		{
			bOK = null != imageFile && imageFile.canRead();
			
			if(bOK)
			{
				imgInStream = ImageIO.createImageInputStream(imageFile);
				bOK = null != imgInStream;
			}
			
			if(bOK)
			{
				iter = ImageIO.getImageReaders(imgInStream);
				bOK  = null != iter && iter.hasNext();
			}
			
	        if (bOK) 
	        {
	            reader = (ImageReader)iter.next();
	            reader.setInput(imgInStream);
	            format = reader.getFormatName();
	        }
		}
		catch(Exception ex)
		{
			throw ex;
		}
        finally
        {
	        if(null != reader)
	        {
	        	reader.dispose();
	        }
	        
	        if(null != imgInStream)
	        {
	        	imgInStream.close();
	        }
        }
		
        return format;
	}
	
	/**
	 * 
	 * @param imageName
	 * @return image file format.
	 */
	public String getImageFileFormat(String imageFileName)
	{
		String fileFormat = "";
		String fileName   = ""; 
		
		if(null != imageFileName && !imageFileName.isEmpty())
		{
			fileName = imageFileName.toLowerCase();
		}
		
	    if(!fileName.isEmpty())
	    {
	    	if(fileName.endsWith(".png"))
	    	{
	    		fileFormat = "png";
	    	}
	    	else if(fileName.endsWith(".gif"))
	    	{
	    		fileFormat = "gif";
	    	}
	    	else if(fileName.endsWith(".tiff"))
	    	{
	    		fileFormat = "tiff";
	    	}
	    	else if(fileName.endsWith(".jpg"))
	    	{
	    		fileFormat = "jpg";
	    	}
	    }
	    return fileFormat;
	}
    
	/**
	 * 
	 * @param filePath
	 * @param content
	 * @return
	 * @throws Exception 
	 */
	public boolean write(String filePath, String content) throws Exception {		
		
		Writer writer = null;
		
		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(filePath), "utf-8"));
		    if(writer != null) {
		    	writer.write(content);
		    }
		} catch (IOException e) {
		  throw e;
		} finally {
		   try {
			   if(writer != null) {
			       writer.flush();
				   writer.close();
				   writer = null;
				   content = null;
				   filePath = null;
			   }
		   } catch (Exception e) {
			   throw e;
		   }
		}
		
		return true;
	}
	
	/**
	 * Delete a folder from filesysyem
	 * @param folder
	 * @return
	 * @throws Exception 
	 */
	public boolean deleteFolder(File folder) throws Exception {
	    
		File[] files = null;
		boolean status = false;
	    
		try {
			if(folder != null && folder.exists()) {
			    files = folder.listFiles();
			    if(files!=null) {
			        for(File file: files) {
			            if(file.isDirectory()) {
			                deleteFolder(file);
			            } else {
			                file.delete();
			            }
			        }
			    }
			    status = folder.delete();
			}
		} catch(Exception ex) {
			throw ex;
		}
		
		return status;
	}
	
	/**
	 * 
	 * @param child
	 * @param level
	 * @return
	 * @throws Exception 
	 */
	public boolean deleteParentIfEmpty(File child, int level) throws Exception {
		
		File parentFile = null;
		boolean status = true;
		
		try {
			if(level > 0) {
				if(child != null && child.exists()) {
					parentFile = child.getParentFile();
					if(child.list().length == 0) {
						status = status && child.delete();
					}
					deleteParentIfEmpty(parentFile, level - 1);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		
		return status;
	}
	
	/**
	 * Copy a folder from one location to another location
	 * @param source
	 * @param destination
	 * @throws Exception
	 */
	public void copy(File source, File destination) throws Exception{
		
		String[] files = null;
		InputStream  inputStream = null;
		OutputStream outputStream = null;

		if(source.isDirectory()) {
			if(!destination.exists()) {
				destination.mkdirs();
			}
			files = source.list();
			for (String file : files) {
				copy(new File(source, file), new File(destination, file));
    		}
		} else {
			try {
				inputStream = new FileInputStream(source);
				outputStream = new FileOutputStream(destination);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = inputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, length);
				}
			} catch(Exception ex) {
				throw ex;
			} finally {
				if(inputStream != null) {
					inputStream.close();
				}
				if(outputStream != null) {
					outputStream.close();
				}
			}
    	}
    }

	/**
	 * 
	 * @param encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}