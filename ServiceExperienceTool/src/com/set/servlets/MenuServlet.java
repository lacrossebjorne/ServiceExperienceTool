package com.set.servlets;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.set.dao.AllergenDAO;
import com.set.dao.DAOFactory;
import com.set.dao.MenuCategoryDAO;
import com.set.dao.MenuDAO;
import com.set.dao.MenuItemDAO;
import com.set.entities.Allergen;
import com.set.entities.Menu;
import com.set.entities.MenuCategory;
import com.set.entities.MenuItem;

/**
 * Servlet implementation class MenuServlet
 */
@WebServlet(
		urlPatterns = {"/menu", "/menu/*"}
		)
public class MenuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MenuServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		
		RestRequest resourceValues = new RestRequest(request.getPathInfo());
		
		List<?> data;
		switch (resourceValues.getResourceType()){
			case MENU:
				if(resourceValues.getMenuId() != null){
					data = getMenuItems(resourceValues.getMenuId(), response);
					/* should probably move the actual fetching of menu items to
					 * \/menu/:id/item and have /menu/:id return just the menu data
					 */
				}else{
					data = getMenuList(response);
				}
				break;
			case CATEGORY:
				data = getCategoryList(resourceValues.getMenuId(), response);
				//TODO handle single category request
				break;
			case ALLERGEN:
				data = getAllergenList(resourceValues.getMenuId());
				//TODO handle single allergen request
				break;
			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(gson.toJson(data));		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private List<MenuItem> getMenuItems(int menuId, HttpServletResponse response) throws IOException{
		DAOFactory daoFactory = DAOFactory.getInstance("local.jndi");
		MenuItemDAO itemDAO = daoFactory.getMenuItemDAO();
		List<MenuItem> items = itemDAO.getItems(menuId);
		return items;
	}
	
	private List<MenuCategory> getCategoryList(int menuId, HttpServletResponse response) throws IOException{
		DAOFactory daoFactory = DAOFactory.getInstance("local.jndi");
		MenuCategoryDAO categoryDAO = daoFactory.getMenuCategoryDAO();
		List<MenuCategory> items = categoryDAO.getCategories(menuId);
		return items;
	}
	
	private List<Menu> getMenuList(HttpServletResponse response) throws ServletException, IOException {
		DAOFactory daoFactory = DAOFactory.getInstance("local.jndi");
		MenuDAO menuDAO = daoFactory.getMenuDAO();
		List<Menu> items = menuDAO.getMenuList();
		return items;
	}
	
	private List<Allergen> getAllergenList(int menuId){
		DAOFactory daoFactory = DAOFactory.getInstance("local.jndi");
		AllergenDAO allergenDAO = daoFactory.getAllergenDAO();
		List<Allergen> allergenList = allergenDAO.getAllergenListByMenu(menuId);
		return allergenList;
	}
	
	
	/*
	 * Inner class for parsing RESTful URLs
	 */
	private class RestRequest {

		private Pattern regExAllPattern = Pattern.compile("/");
		private Pattern regExIdPattern = Pattern.compile("/([0-9]+)");
		private Pattern regExCategoryAllPattern = Pattern.compile("/([0-9]+)/category");
		private Pattern regExCategoryIdPattern = Pattern.compile("/([0-9]+)/category/([0-9]+)");
		private Pattern regExAllergenAllPattern = Pattern.compile("/([0-9]+)/allergen");
		private Pattern regExAllergenIdPattern = Pattern.compile("/([0-9]+)/allergen/([0-9]+)");
		

		private Integer menuId;
		private Integer categoryId;
		private Integer allergenId;
		private ResourceType resourceType;

		public RestRequest(String pathInfo) throws ServletException {

			if(pathInfo == null){
				resourceType = ResourceType.MENU;
				return;
			}
			Matcher matcher;

			matcher = regExCategoryIdPattern.matcher(pathInfo);
			if (matcher.find()) {
				menuId = Integer.parseInt(matcher.group(1));
				categoryId = Integer.parseInt(matcher.group(2));
				resourceType = ResourceType.CATEGORY;
				return;
			}
			
			matcher = regExCategoryAllPattern.matcher(pathInfo);
			if (matcher.find()) {
				menuId = Integer.parseInt(matcher.group(1));
				resourceType = ResourceType.CATEGORY;
				return;
			}
			
			matcher = regExAllergenIdPattern.matcher(pathInfo);
			if (matcher.find()) {
				menuId = Integer.parseInt(matcher.group(1));
				allergenId = Integer.parseInt(matcher.group(2));
				resourceType = ResourceType.ALLERGEN;
				return;
			}
			
			matcher = regExAllergenAllPattern.matcher(pathInfo);
			if (matcher.find()) {
				menuId = Integer.parseInt(matcher.group(1));
				resourceType = ResourceType.ALLERGEN;
				return;
			}

			matcher = regExIdPattern.matcher(pathInfo);
			if (matcher.find()) {
				menuId = Integer.parseInt(matcher.group(1));
				resourceType = ResourceType.MENU;
				return;
			}

			matcher = regExAllPattern.matcher(pathInfo);
			if (matcher.find()){
				resourceType = ResourceType.MENU;
				return;
			}

			resourceType = ResourceType.UNDEFINED;
		}

		public Integer getMenuId() {
			return menuId;
		}
		public Integer getCategoryId() {
			return categoryId;
		}
		public Integer getAllergenId() {
			return allergenId;
		}

		public ResourceType getResourceType(){
			return this.resourceType;
		}
	}
	
	private enum ResourceType {
		CATEGORY,
		MENU,
		ALLERGEN,
		UNDEFINED;
	}

}
