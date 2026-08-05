package fr.shuvly.paper.madgui;

import fr.shuvly.paper.madgui.handler.click.ClickHandler;
import fr.shuvly.paper.madgui.manager.MadGuiManager;
import fr.shuvly.paper.madgui.page.Page;
import fr.shuvly.paper.maditem.MadItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PaginatedMadGui extends MadGui
{

    private List<Page> pages;
    private List<Integer> freeSlotIndexes;
    private int currentPageIndex;

    private final MadGuiManager guiManager;


    /**
     * Creates a new Paginated GUI.
     *
     * @param	title	    GUI's title
     * @param	size	    GUI's amount of lines (must be between 1 and 6).
     * @param   guiManager  GUI manager
     */
    public PaginatedMadGui(Component title, int size, MadGuiManager guiManager)
    {
        super(title, size);

        this.guiManager = guiManager;
        this.setup();
    }

    /**
     * Creates a new Paginated GUI from an existing Inventory.
     *
     * @param	inventory	Inventory
     * @param   guiManager  Gui manager
     */
    public PaginatedMadGui(Inventory inventory, MadGuiManager guiManager)
    {
        super(inventory);

        this.guiManager = guiManager;
        this.setup();
    }


    /**
     * Setups the Paginated GUI.
     */
    private void setup()
    {
        this.pages = new ArrayList<>();
        this.createNewPage();
        this.setCurrentPageIndex(0);
    }

    /**
     * Finds and puts in a list every free slot indexes.
     */
    private void findFreeSlotIndexes()
    {
        this.freeSlotIndexes = new ArrayList<>();

        for (int k = 0; k < super.getTotalSize(); k++) {
            final ItemStack item = super.getInventory().getItem(k);

            if (item == null || item.getType() == Material.AIR) {
                this.freeSlotIndexes.add(k);
            }
        }
    }

    /**
     * Sets the item as the previous page button.
     * FIXME: In the click action, if gui is not an instance of
     *        PaginatedMadGui, we may want to throw an error.
     *
     * @param   item        Item
     * @return  Itself
     */
    public PaginatedMadGui setPreviousPageItem(MadItem item)
    {
        item.addClickHandler(
            new ClickHandler()
                .addAllClickTypes()
                .setClickAction((Player player, MadGui gui, ItemStack i, int slot) -> {
                    if (!(gui instanceof PaginatedMadGui paginatedGui)) {
                        return;
                    }

                    final int pageIndex = paginatedGui.getCurrentPageIndex() - 1;

                    if (pageIndex < 0) {
                        return;
                    }
                    paginatedGui.setCurrentPageIndex(pageIndex);
                    paginatedGui.update(player);
                })
        );

        item.build(this, this.guiManager);
        return this;
    }

    /**
     * Sets the item as the next page button.
     * FIXME: In the click action, if gui is not an instance of
     *        PaginatedMadGui, we may want to throw an error.
     *
     * @param   item        Item
     * @return  Itself
     */
    public PaginatedMadGui setNextPageItem(MadItem item)
    {
        item.addClickHandler(
            new ClickHandler()
                .addAllClickTypes()
                .setClickAction((Player player, MadGui gui, ItemStack i, int slot) -> {
                    if (!(gui instanceof PaginatedMadGui paginatedGui)) {
                        return;
                    }

                    final int pageIndex = paginatedGui.getCurrentPageIndex() + 1;

                    if (pageIndex >= this.pages.size()) {
                        return;
                    }
                    paginatedGui.setCurrentPageIndex(pageIndex);
                    paginatedGui.update(player);
                })
        );

        item.build(this, this.guiManager);
        return this;
    }

    @Override
    public void open(Player player)
    {
        super.open(player);
        this.update(player);
    }

    /**
     * Updates the GUI according to the current page.
     *
     * @param   player  Player
     * @return  Itself
     */
    @Override
    public PaginatedMadGui update(Player player)
    {
        final Page currentPage = this.getCurrentPage();

        this.clearPageContent();

        for (int slot : currentPage.getItems().keySet()) {
            final MadItem item = currentPage.getItem(slot);
            super.setItem(slot, item);
        }
        super.update(player);
        return this;
    }

    /**
     * Visually clears every free slot.
     *
     * @return  Itself
     */
    public PaginatedMadGui clearPageContent()
    {
        if (this.freeSlotIndexes == null) {
            this.findFreeSlotIndexes();
        }

        for (int k : this.freeSlotIndexes) {
            super.getInventory().setItem(k, null);
        }
        return this;
    }

    /* Page management */

    /**
     * Adds a new page to the Gui.
     *
     * @param   page    Page to add
     * @return  Itself
     */
    public PaginatedMadGui addPage(Page page)
    {
        this.pages.add(page);
        return this;
    }

    /**
     * Creates a fresh new page.
     *
     * @return  Created page
     */
    public Page createNewPage()
    {
        final Page page = new Page(this);

        this.addPage(page);
        return page;
    }

    /**
     * Clears all pages.
     *
     * @return  Itself
     */
    public PaginatedMadGui clearPages()
    {
        this.clearPageContent();
        this.pages.clear();
        this.createNewPage();
        this.setCurrentPageIndex(0);
        return this;
    }

    /**
     * @param   index  Page index
     * @return  Page at index
     */
    public Page getPage(int index)
    {
        return this.pages.get(index);
    }

    /**
     * @return  All pages
     */
    public List<Page> getPages()
    {
        return this.pages;
    }

    /**
     * @return  Last page
     */
    public Page getLastPage()
    {
        final int pagesAmount = this.pages.size();

        return this.getPages().get(pagesAmount == 0 ? 0 : pagesAmount - 1);
    }

    /**
     * @return  Current page
     */
    public Page getCurrentPage()
    {
        return this.getPage(this.currentPageIndex);
    }

    /**
     * @return  Free slots indexes
     */
    private List<Integer> getFreeSlotIndexes()
    {
        return this.freeSlotIndexes;
    }

    /**
     * @return  Current page index
     */
    public int getCurrentPageIndex()
    {
        return this.currentPageIndex;
    }

    /**
     * @param   index   Page index
     */
    public void setCurrentPageIndex(int index)
    {
        this.currentPageIndex = index;
    }

}
