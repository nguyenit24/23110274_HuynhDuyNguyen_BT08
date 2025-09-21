package vn.iot.model;

public class CategoryModel {
	private Long CategoryId;
	private String name;
	private boolean isEdit;

	public Long getCategoryId() {
		return CategoryId;
	}

	public void setCategoryId(Long categoryId) {
		CategoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean edit) {
		this.isEdit = edit;
	}

}
