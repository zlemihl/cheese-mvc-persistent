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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

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
        return "index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("title","Add a new menu");
        model.addAttribute(new Menu());
        return "add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid Menu menu, Errors errors){
        if(errors.hasErrors()){
            model.addAttribute("title", "Add a new menu");
            model.addAttribute(menu);
            return "add";
        } else {
            menuDao.save(menu);
            return "redirect:view/" + menu.getId();
        }
    }

    @RequestMapping(value = "view/${menuId}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int menuId) {
        model.addAttribute("menu", menuDao.findOne(menuId));
        return "view";
    }

    @RequestMapping(value = "add-item/${menuId}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int menuId) {
        Menu amenu = menuDao.findOne(menuId);
        Iterable<Cheese> cheeses = cheeseDao.findAll();
        AddMenuItemForm form = new AddMenuItemForm(amenu, cheeses);
        model.addAttribute(form);
        return "add-item";
    }

    @RequestMapping(value = "add-item/${menuId}", method = RequestMethod.POST)
    public String processItem(Model model, @PathVariable int menuId,
                              @ModelAttribute @Valid AddMenuItemForm form, Errors errors) {
        if(errors.hasErrors()){
            model.addAttribute(form);
            return "add-item";
        }
        Cheese z = cheeseDao.findOne(form.getCheeseId());
        Menu x = menuDao.findOne(menuId);
        x.addItem(z);
        menuDao.save(x);
        return "redirect:view/" + x.getId();
    }
}
