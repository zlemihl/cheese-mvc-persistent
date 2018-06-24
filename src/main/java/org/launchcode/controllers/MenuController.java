package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    MenuDao menuDao;

    @Autowired
    CheeseDao cheeseDao;

    @RequestMapping(value = "")
    public String index(Model model){
        model.addAttribute("menus",menuDao.findAll());
        model.addAttribute("title","Menus");
        model.addAttribute("variable","menu");
        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("title","Add a new menu");
        model.addAttribute(new Menu());
        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid Menu menu, Errors errors){
        if(errors.hasErrors()){
            model.addAttribute("title", "Add a new menu");
            model.addAttribute(menu);
            return "menu/add";
        } else {
            menuDao.save(menu);
            return "redirect:view/" + menu.getId();
        }
    }

    @RequestMapping(value = "view/{menuId}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int menuId) {
        Menu amenu = menuDao.findOne(menuId);
        model.addAttribute("title", amenu.getName());
        model.addAttribute("menuId", amenu.getId());
        model.addAttribute("cheeses", amenu.getCheeses());
        return "menu/view";
    }

    @RequestMapping(value = "add-item/{menuId}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int menuId) {
        Menu amenu = menuDao.findOne(menuId);
        AddMenuItemForm form = new AddMenuItemForm(amenu, cheeseDao.findAll());
        model.addAttribute("title", "Add a cheese to " + amenu.getName());
        model.addAttribute("form", form);
        return "menu/add-item";
    }

    @RequestMapping(value = "add-item", method = RequestMethod.POST)
    public String processItem(Model model, @ModelAttribute @Valid AddMenuItemForm form, Errors errors) {
        if(errors.hasErrors()){
            model.addAttribute("form", form);
            return "menu/add-item";
        }
        Cheese z = cheeseDao.findOne(form.getCheeseId());
        Menu x = menuDao.findOne(form.getMenuId());
        x.addItem(z);
        menuDao.save(x);
        return "redirect:view/" + x.getId();
    }
}
