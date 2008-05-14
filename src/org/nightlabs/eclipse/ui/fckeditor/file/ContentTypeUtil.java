package org.nightlabs.eclipse.ui.fckeditor.file;

import org.nightlabs.eclipse.ui.fckeditor.IFCKEditorContentFile;

/**
 * @author Marc Klinger - marc[at]nightlabs[dot]de
 */
public abstract class ContentTypeUtil
{
	public static final String IMAGE_PNG = "image/png";
	public static final String IMAGE_GIF = "image/gif";
	public static final String IMAGE_JPEG = "image/jpeg";

	// first entry in a list of equal content types is default extension
	private static String[][] contentTypes = new String[][] {
		{ "application/pdf", 	       ".pdf" },
		{ IMAGE_JPEG,                  ".jpg" },
		{ IMAGE_JPEG,                  ".jpeg" },
		{ IMAGE_GIF,                   ".gif" },
		{ IMAGE_PNG,                   ".png" },
		{ "text/html",                 ".html" },
		{ "text/html",                 ".htm" },
		{ "text/css",                  ".css" },
		{ "text/plain",                ".txt" },
		{ "text/plain",                ".asc" },
		{ "text/xml",                  ".xml" },
		{ "text/javascript",           ".js" },
		{ "audio/mpeg",                ".mp3" },
		{ "audio/mpeg-url",            ".m3u" },
		{ "application/msword",        ".doc" },
		{ "application/x-ogg",         ".ogg" },
		{ "application/octet-stream",  ".bin" },
		{ "application/octet-stream",  ".zip" },
		{ "application/octet-stream",  ".exe" },
		{ "application/octet-stream",  ".class" },
		{ "application/unknown",       ".bin" },
	};

	public static String getFileExtension(String contentType)
	{
		if(contentType != null) {
			String _contentType = contentType.toLowerCase();
			for (String[] pair : contentTypes) {
				if(pair[0].equals(_contentType))
						return pair[1];
			}
		}
		return ".bin";
	}

	public static String getContentType(String fileName)
	{
		if(fileName != null) {
			String _fileName = fileName.toLowerCase();
			for (String[] pair : contentTypes) {
				if(_fileName.endsWith(pair[1]))
						return pair[0];
			}
		}
		return "application/unknown";
	}

	public static String getFileExtension(IFCKEditorContentFile file)
	{
		return getFileExtension(file.getContentType());
	}

	public static String getContentType(IFCKEditorContentFile file)
	{
		return getContentType(file.getContentType());
	}
}
