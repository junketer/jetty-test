package com.djt.app.to;

import java.io.IOException;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;


public class SimpleDataItem extends AbstractDataItem {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static DataItem create(long id, Message msg) throws MessagingException, IOException {
		System.out.println("subject:"
						+ msg.getSubject());
		System.out.println("from: " + msg.getFrom()[0].toString());
		System.out.println("sent: " + msg.getSentDate());
		msg.setFlag(Flags.Flag.DELETED, true);
		System.out.println("msg is expunged: " + msg.isExpunged());
		System.out.println("msg mime type: " + msg.getContentType());
		String data = null;
		if (msg.getContent() != null) {
			System.out.println(" msg content type: "
					+ msg.getContent().getClass().getName());
			System.out.println(" msg content: " + msg.getContent());
			Object partContent = msg.getContent();
			if (partContent instanceof String) {
				data = (String) partContent;
			} else if (partContent instanceof MimeMultipart) {
				MimeMultipart mimeMultiPart = (MimeMultipart) partContent;
				try {
					BodyPart part;
					int count = mimeMultiPart.getCount();
					for (int partSeq = 0; partSeq < count; partSeq++) {
						part = mimeMultiPart.getBodyPart(partSeq);
						System.out.println(" mime multipart body party: "
								+ part);
						System.out.println(" body part content: "
								+ part.getContent());
						if (part.getContent() != null
								&& !part.getContent().toString()
										.equals(data)) {
							if (data == null) {
								data = part.getContent().toString();
							} else {
								data = data
										+ part.getContent()
												.toString();
							}
						}
					}
				} catch (MessagingException e) {
					e.printStackTrace(System.out);
				}
			}
		}
		return new SimpleDataItem(id, msg.getSubject(), data);

	}
	public SimpleDataItem(long id, String description, String data) {
		super(id,description,data);
	}

}
