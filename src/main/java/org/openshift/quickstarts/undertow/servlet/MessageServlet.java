package org.openshift.quickstarts.undertow.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class MessageServlet extends HttpServlet {

    public static final String MESSAGE = "message";

    private String message;

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        message = config.getInitParameter(MESSAGE);
    }

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String file = request.getParameter("file");
        String count = request.getParameter("count");
        if (file == null) {
            PrintWriter pw = response.getWriter();
            if (count == null) {
                pw.write("parameter file or count is required");
            } else {
                pw.write("<html><body><ul>");
                for (int i = 1001; i <= 1000 + Integer.parseInt(count); i++) {
                    pw.writw("<li><a href=\"#?file=" + i + "\" target=\"_blank\">" + i + "</a></li>");
                }
                pw.write("</ul></body></html>");
            }
            pw.close();
            return;
        }
		String filePath = "/tmp/download/" + file;
		File downloadFile = new File(filePath);

		String mimeType = "application/octet-stream";
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());

		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		response.setHeader(headerKey, headerValue);

		try (FileInputStream inStream = new FileInputStream(downloadFile)) {
			try (OutputStream outStream = response.getOutputStream()) {
				byte[] buffer = new byte[4096];
				int bytesRead = -1;
				while ((bytesRead = inStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}
			}
		}
	}

}
