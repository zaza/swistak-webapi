package com.swistak.webapi.scanner;

import static com.google.common.base.Preconditions.checkState;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Stack;

import com.google.common.base.CaseFormat;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import com.swistak.webapi.Auction_costs_delivery;
import com.swistak.webapi.Auction_parameter;
import com.swistak.webapi.category.Category;
import com.swistak.webapi.category.Tree;
import com.swistak.webapi.category.Tree.TreeNode;
import com.swistak.webapi.model.AuctionParams;
import com.swistak.webapi.model.AuctionType;
import com.swistak.webapi.model.AuctionUnit;
import com.swistak.webapi.model.ConditionProduct;
import com.swistak.webapi.model.DeliveryInfo;
import com.swistak.webapi.model.Province;
import com.swistak.webapi.model.WhoPayment;

public class AuctionFolder implements AuctionParams {

	private final File folder;

	private final Tree<Category> tree;
	
	private Properties properties;

	private int category = Category.UNKNOWN.getId();

	private List<File> photos;

	public AuctionFolder(File folder, Tree<Category> tree) {
		this.folder = folder;
		this.tree = tree;
	}

	public boolean hasDescription() {
		return getDescriptionFile().exists();
	}

	private File getDescriptionFile() {
		return new File(folder, "opis.txt");
	}

	public boolean hasParameters() {
		return getParametersFile().exists();
	}

	private File getParametersFile() {
		return new File(folder, "parametry.properties");
	}
	
	private Properties getProperties() {
		try {
			if (properties == null) {
				FileInputStream fis = new FileInputStream(getParametersFile());
				BufferedReader in = new BufferedReader(new InputStreamReader(fis, Charsets.UTF_8.name()));
				properties = new Properties();
				properties.load(in);
				in.close();
			}
			return properties;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getTitle() {
		return getProperties().getProperty("tytuł");
	}

	@Override
	public float getPrice() {
		return Float.parseFloat(getProperties().getProperty("cena"));
	}

	@Override
	public AuctionType getType() {
		// TODO:
		throw new UnsupportedOperationException();
	}

	@Override
	public int getCategory() {
		String kategoria = getProperties().getProperty("kategoria");
		if (kategoria != null) {
			List<String> categories = Splitter.on(':').trimResults()
					.splitToList(kategoria);
			Stack<String> stack = new Stack<String>();
			for (int i = categories.size() - 1; i >= 0; i--) {
				stack.push(categories.get(i));
			}
			category = findInChildren(tree.getRoot().getChildren(), stack);
		}
		return category;
	}
	
	private int findInChildren(List<TreeNode<Category>> children, Stack<String> stack) {
		String category = stack.pop();
		for (TreeNode<Category> child : children) {
			if (child.getData().getName().equals(category)) {
				if (child.getChildren().isEmpty())
					return child.getData().getId();
				return findInChildren(child.getChildren(), stack);
			}
		}
		return Category.UNKNOWN.getId();
	}

	@Override
	public String getCity() {
		return getProperties().getProperty("miasto");
	}

	@Override
	public ConditionProduct getCondition() {
		String stan = getProperties().getProperty("stan");
		switch (stan) {
			case "nowe" :
				return ConditionProduct.nowy;
			case "nowa" :
				return ConditionProduct.nowy;
			case "używane" :
				return ConditionProduct.uzywany;
			case "uzywane" :
				return ConditionProduct.uzywany;
			case "używana" :
				return ConditionProduct.uzywany;
		}
		return ConditionProduct.valueOf(stan);
	}

	@Override
	public DeliveryInfo[] getDeliveryInfo() {
		// TODO:
		throw new UnsupportedOperationException();
	}

	@Override
	public String getDescription() {
		try {
			return Files.asCharSource(getDescriptionFile(), Charsets.UTF_8).read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getCount() {
		return Integer.parseInt(getProperties().getProperty("sztuki"));
	}

	@Override
	public Auction_parameter[] getParamaters() {
		// TODO:
		throw new UnsupportedOperationException();
	}

	@Override
	public Auction_costs_delivery getCostsDelivery() {
		// TODO:
		throw new UnsupportedOperationException();
	}

	@Override
	public AuctionUnit getUnit() {
		if (getProperties().containsKey("sztuki"))
			return AuctionUnit.sztuki;
		else if (getProperties().containsKey("komplety"))
			return AuctionUnit.komplety;
		else if (getProperties().containsKey("pary"))
			return AuctionUnit.pary;
		throw new IllegalArgumentException("Property for AuctionUnit not found");
	}

	@Override
	public WhoPayment getWhoPayment() {
		// TODO:
		throw new UnsupportedOperationException();
	}

	@Override
	public Province getProvince() {
		return Province.valueOf(CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, getProperties().getProperty("województwo")));
	}

	@Override
	public String getTags() {
		// TODO:
		throw new UnsupportedOperationException();
	}

	public boolean hasCategory() {
		return getCategory() != Category.UNKNOWN.getId();
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public List<File> getPhotos() {
		return this.photos;
	}

	public boolean hasId() {
		return getProperties().containsKey("id");
	}

	public long getId() {
		checkState(hasId());
		return Long.parseLong(getProperties().getProperty("id"));
	}

	public void setPhotos(File[] photos) {
		this.photos = Arrays.asList(photos);
	}

	public void setId(long id) {
		getProperties().put("id", Long.toString(id));
	}

	public void save() {
		try {
			FileOutputStream fos = new FileOutputStream(getParametersFile());
			OutputStreamWriter osw = new OutputStreamWriter(fos, Charsets.UTF_8);
			getProperties().store(osw, null);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
