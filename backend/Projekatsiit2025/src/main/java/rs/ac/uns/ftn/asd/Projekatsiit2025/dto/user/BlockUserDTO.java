package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class BlockUserDTO {
	@Positive(message = "Id must be positive")
	@NotNull
	private Long id;

	@Size(max = 500, message = "Block reason too long")
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
