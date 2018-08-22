package ch.htwchur.coltero.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loader for resource files in bundled jar
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class QuerieLoader {
	private static final Logger log = LoggerFactory.getLogger(QuerieLoader.class);
	private static final String FOLDER_PREFIX = "/sql/";

	public QuerieLoader() {

	}

	/**
	 * Loads file from Bundled jar from resources/sql/
	 * 
	 * @param fileName
	 * @return
	 */
	public String loadQuerie(String fileName) {
		// this classloader util tries several classloaders for loading a resource file
		InputStream is = com.atlassian.core.util.ClassLoaderUtils.getResourceAsStream(FOLDER_PREFIX + fileName,
				this.getClass());
		@SuppressWarnings("resource")
		Scanner s = new Scanner(is).useDelimiter("\\A");
		String result = s.hasNext() ? s.next() : "";
		try {
			is.close();
		} catch (IOException e) {
			log.error("Could not close inputstream: " + e.getMessage() + " " + e.getCause());
		} finally {
			s.close();
		}
		return result;

	}
	/**
	 * Loads file from resource root
	 * @param fileName
	 * @return
	 */
	public String loadFile(String fileName) {
		// this classloader util tries several classloaders for loading a resource file
		InputStream is = com.atlassian.core.util.ClassLoaderUtils.getResourceAsStream(fileName,
				this.getClass());
		@SuppressWarnings("resource")
		Scanner s = new Scanner(is).useDelimiter("\\A");
		String result = s.hasNext() ? s.next() : "";
		try {
			is.close();
		} catch (IOException e) {
			log.error("Could not close inputstream: " + e.getMessage() + " " + e.getCause());
		} finally {
			s.close();
		}
		return result;

	}
}
