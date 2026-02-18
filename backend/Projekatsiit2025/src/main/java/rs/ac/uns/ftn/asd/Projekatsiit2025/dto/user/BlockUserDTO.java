package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user;

public class BlockUserDTO {
	private Long id;
	private String blockReason;
	public BlockUserDTO() {
		super();
	}
	public BlockUserDTO(Long id, String blockReason) {
		super();
		this.id = id;
		this.blockReason = blockReason;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBlockReason() {
		return blockReason;
	}
	public void setBlockReason(String blockReason) {
		this.blockReason = blockReason;
	}
	
}
