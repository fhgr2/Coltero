package ch.htwchur.coltero.api.dto.page;
/**
 * Class for reactions
 * @author sandro.hoerler@htwchur.ch
 *
 */

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
public class ReactionsDTO {

	private int reactions;

	public ReactionsDTO(int reactions) {
		super();
		this.reactions = reactions;
	}

	public int getReactions() {
		return reactions;
	}

	public void setReactions(int reactions) {
		this.reactions = reactions;
	}

}
