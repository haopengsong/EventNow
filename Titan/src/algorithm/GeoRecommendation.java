package algorithm;

import java.util.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;

public class GeoRecommendation {
	public List<Item> recommenditems(String userId, double lat, double lon) {
		List<Item> recommendeditems = new ArrayList<>();
		DBConnection conn = DBConnectionFactory.getDBConnection();
		
		//get all categories
		Set<String> favoriteitemsId = conn.getFavoriteItemIds(userId);
		//get all categories of favorite items sort by count
		Map<String, Integer> allcategories = new HashMap<>();
		for (String id : favoriteitemsId) {
			Set<String> cates = conn.getCategories(id);
			for (String category : cates) {
				if (allcategories.containsKey(category)) {
					allcategories.put(category, allcategories.get(category) + 1);
				} else {
					allcategories.put(category, 1);
				}
			}
		}
		List<Entry<String, Integer>> categoryList = new ArrayList<Entry<String, Integer>>(allcategories.entrySet());
		Collections.sort(categoryList, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return Integer.compare(o2.getValue(), o1.getValue());
			}
		});
		//comparator return value, -1 = 不换位置， 1 = 换位置，0 = 相等，不用变
		Set<Item> visiteditem = new HashSet<>();
		//先搜category权重大的
		for (Entry<String, Integer> category : categoryList) {
			List<Item> items = conn.searchItems(lat, lon, category.getKey());
			List<Item> filtereditems = new ArrayList<>();
			for (Item item : items) {
				if (!favoriteitemsId.contains(item.getItemId()) && !visiteditem.contains(item)) {
					filtereditems.add(item);
				}
			}
			Collections.sort(filtereditems, new Comparator<Item>() {

				@Override
				public int compare(Item o1, Item o2) {
					// TODO Auto-generated method stub
					return Double.compare(o1.getDistance(), o2.getDistance());
				}
			});
			visiteditem.addAll(items);
			recommendeditems.addAll(filtereditems);
			
		}
		return recommendeditems;
	}
}
