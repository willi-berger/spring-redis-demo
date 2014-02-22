package at.wberger.springredisdemo.core.domain;

public class Permission {
	


	private int created;
	
	private int validFrom;
	
	private int validTo;
	
	private String text;

	
	public Permission(int created, int validFrom, int validTo, String text) {
		super();
		this.created = created;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.text = text;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public int getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(int validFrom) {
		this.validFrom = validFrom;
	}

	public int getValidTo() {
		return validTo;
	}

	public void setValidTo(int validTo) {
		this.validTo = validTo;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return String.format("Permission[text=%s, validFrom=%s, validTo=%s]", text, validFrom, validTo);
	}
}
