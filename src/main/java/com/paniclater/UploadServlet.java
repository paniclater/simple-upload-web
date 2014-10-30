package com.paniclater;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet(name = "uploadServlet", urlPatterns = "/upload", loadOnStartup = 1)
@MultipartConfig(fileSizeThreshold = 5_242_880, // 5MB
maxFileSize = 20_971_520L, // 20MB
maxRequestSize = 41_943_040L // 40MB
)
public class UploadServlet extends HttpServlet {
	private Map<Integer, UploadedFile> uploadedFileDatabase = new LinkedHashMap<>();
	private volatile int FILE_ID_SEQUENCE = 0;

	@Override
	public void destroy() {
		System.out.println("Stopping upload servlet");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = req.getParameter("action");
		if (action == null)
			action = "list";
		switch (action) {
		case "upload":
			this.showForm(req, resp);
			break;
		case "download":
			this.downloadFile(req, resp);
			break;
		case "list":
		default:
			this.listUploadedFiles(req, resp);
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = req.getParameter("action");
		switch (action) {
		case "upload":
			this.uploadFile(req, resp);
			break;
		}
		resp.sendRedirect("upload");
	}

	private void downloadFile(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		UploadedFile file = this.uploadedFileDatabase.get(id);
		if (file == null)
			return;
		resp.setHeader("Content-Disposition",
				"attachment; filename=" + file.getName());
		resp.setContentType("application/octet-stream");

		ServletOutputStream stream = resp.getOutputStream();
		stream.write(file.getContents());
	}

	private byte[] filePartToByteArray(Part filePart) throws IOException {
		InputStream inputStream = filePart.getInputStream();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		int read;
		final byte[] bytes = new byte[1024];

		while ((read = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}

		return outputStream.toByteArray();
	}

	@Override
	public void init() throws ServletException {
		System.out.println("Starting upload servlet");
	}

	private void listUploadedFiles(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		System.out.println("Listing uploaded Files");
		req.setAttribute("uploadedFileDatabase", this.uploadedFileDatabase);
		req.getRequestDispatcher("/WEB-INF/jsp/view/listUploadedFiles.jsp").forward(req, resp);
//		PrintWriter writer = this.writeHeader(resp);
//		writer.append("<h2>Uploaded Files</h2>\r\n");
//		writer.append("<a href=\"upload?action=upload\">Upload A File").append(
//				"</a><br/><br/>\r\n");
//		if (this.uploadedFileDatabase.size() == 0
//				|| this.uploadedFileDatabase == null) {
//			writer.append("<p>There have been no uploaded files</p>");
//		} else {
//			writer.append("<ul>");
//			for (UploadedFile file : this.uploadedFileDatabase.values()) {
//				writer.append("<li>" + file.getName()
//						+ " <a href=\"upload?action=download&id="
//						+ file.getId() + "\">download</a></li>");
//			}
//			writer.append("</ul>");
//		}
//		this.writeFooter(writer);
	}

	private void showForm(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		req.getRequestDispatcher("/WEB-INF/jsp/view/uploadFileForm.jsp")
				.forward(req, resp);
	}

	private void uploadFile(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		UploadedFile file = new UploadedFile();
		file.setName(req.getParameter("fileName"));
		Part filePart = req.getPart("file");
		if (filePart == null || filePart.getSize() <= 0) {
			throw new IOException("No file or empty file");
		}
		try {
			file.setContents(filePartToByteArray(filePart));
		} catch (IOException e) {
			System.out.println("No file or empty file");
		}
		int id;
		synchronized (this) {
			id = this.FILE_ID_SEQUENCE++;
			file.setId(id);
			this.uploadedFileDatabase.put(id, file);
		}
	}

	private void writeFooter(PrintWriter writer) {
		writer.append("</body></html>");
	}

	private PrintWriter writeHeader(HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = resp.getWriter();
		writer.append("<!doctype html><html><head><title>Simple Upload Web</title></head><body>");
		return writer;
	}

}
