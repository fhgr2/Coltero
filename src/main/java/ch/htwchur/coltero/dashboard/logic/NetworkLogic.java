package ch.htwchur.coltero.dashboard.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import ch.htwchur.coltero.api.dto.space.network.Links;
import ch.htwchur.coltero.api.dto.space.network.NetworkModel;
import ch.htwchur.coltero.api.dto.space.network.Node;
import ch.htwchur.coltero.api.dto.tag.TagNeighbourDTO;

/**
 * Class for calculating neighbour Entities and to create network data for
 * visualization
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class NetworkLogic {

	public NetworkLogic() {
	}

	/**
	 * Calculate Neighbour-Entities by checking for same tags in given entity, but
	 * there can be more tags as the tags from the defined space (but all tags
	 * defined by the given entity must be included)
	 * 
	 * @param allEntities
	 * @param theEntity
	 * @param entityId
	 * @return
	 */
	public NetworkModel calcNeighbourEntities(List<TagNeighbourDTO> allEntities, List<TagNeighbourDTO> theEntity,
			int entityId) {
		// if the defined entity has no tags, skip calculation and return empty Result
		if (theEntity.isEmpty()) {
			return new NetworkModel(null, null);
		}
		Set<Integer> uniqueIdSet = new HashSet<>();
		allEntities.forEach(item -> uniqueIdSet.add(item.getEntityId()));

		Map<Integer, List<TagNeighbourDTO>> tagMap = new HashMap<>();
		Iterator<Integer> itr = uniqueIdSet.iterator();
		while (itr.hasNext()) {
			int spaceId = itr.next();
			List<TagNeighbourDTO> entityTagList = new ArrayList<>();
			for (TagNeighbourDTO item : allEntities) {
				if (item.getEntityId() == spaceId) {
					entityTagList.add(item);
				}
			}
			tagMap.put(spaceId, entityTagList);
		}
		// filter all entities with less then tag-count
		tagMap = tagMap.entrySet().stream().filter(x -> x.getValue().size() >= theEntity.size())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		// Check if some entities has at least the same tags as the defined entity
		Iterator<Entry<Integer, List<TagNeighbourDTO>>> tagMapItr = tagMap.entrySet().iterator();
		while (tagMapItr.hasNext()) {
			if (!tagMapItr.next().getValue().containsAll(theEntity)) {
				tagMapItr.remove();
			}

		}
		String entityName = null;
		if (!theEntity.isEmpty()) {
			entityName = theEntity.get(0).getEntityName();
		}
		List<TagNeighbourDTO> resultList = new ArrayList<>();
		tagMap.entrySet().forEach(x -> resultList.addAll(x.getValue()));
		return calcTagNetwork(resultList, entityName);

	}

	/**
	 * Calculate Networkmodel
	 * 
	 * @param neighbourEntities
	 * @param theSpace
	 * @return
	 */
	private NetworkModel calcTagNetwork(List<TagNeighbourDTO> neighbourEntities, String entityName) {
		Set<Node> nodes = new HashSet<>();
		neighbourEntities.forEach(x -> {
			nodes.add(new Node(x.getEntityName(), 0));
		});
		// add entities to Nodes
		nodes.add(new Node(entityName, 0));
		List<Links> links = new ArrayList<>();
		nodes.forEach(x -> {
			if (!entityName.equals(x.getName())) {
				links.add(new Links(entityName, x.getName(), null, 0)); // null -> no username available
			}
		});

		return new NetworkModel(new ArrayList<Node>(nodes), links);
	}

	/**
	 * Create User-Connection Networkdiagram
	 * 
	 * @param connections
	 * @param theUsername
	 *            root user
	 * @return
	 */
	public NetworkModel calcConnectionNetwork(List<String> connections, String theUsername) {
		List<Node> nodes = new ArrayList<>();
		connections.forEach(item -> nodes.add(new Node(item, 0)));
		nodes.add(new Node(theUsername, 0));
		List<Links> links = new ArrayList<>();
		connections.forEach(item -> links.add(new Links(theUsername, item, "", 0))); // no username like just links
																						// between users
		return new NetworkModel(nodes, links);

	}
}
