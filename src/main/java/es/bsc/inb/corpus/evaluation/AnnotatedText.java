package es.bsc.inb.corpus.evaluation;

public class AnnotatedText {
	
	public AnnotatedText(String text, String label) {
		super();
		this.text = text;
		this.label = label;
	}

	private String text;
	
	private String label;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
}
