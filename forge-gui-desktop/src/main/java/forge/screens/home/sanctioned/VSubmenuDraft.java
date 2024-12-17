package forge.screens.home.sanctioned;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

import forge.game.GameType;
import forge.gui.framework.DragCell;
import forge.gui.framework.DragTab;
import forge.gui.framework.EDocID;
import forge.itemmanager.DeckManager;
import forge.itemmanager.ItemManagerContainer;
import forge.localinstance.properties.ForgeConstants;
import forge.screens.deckeditor.CDeckEditorUI;
import forge.screens.home.EMenuGroup;
import forge.screens.home.IVSubmenu;
import forge.screens.home.LblHeader;
import forge.screens.home.StartButton;
import forge.screens.home.VHomeUI;
import forge.screens.home.VHomeUI.PnlDisplay;
import forge.toolbox.FLabel;
import forge.toolbox.FRadioButton;
import forge.toolbox.FSkin;
import forge.toolbox.JXButtonPanel;
import forge.util.Localizer;
import net.miginfocom.swing.MigLayout;

/** 
 * Assembles Swing components of draft submenu singleton.
 *
 * <br><br><i>(V at beginning of class name denotes a view class.)</i>
 */
public enum VSubmenuDraft implements IVSubmenu<CSubmenuDraft> {
    /** */
    SINGLETON_INSTANCE;
    final Localizer localizer = Localizer.getInstance();
    // Fields used with interface IVDoc
    private DragCell parentCell;

    private final DragTab tab = new DragTab(localizer.getMessage("lblBoosterDraft"));

    /** */
    private final LblHeader lblTitle = new LblHeader(localizer.getMessage("lblHeaderBoosterDraft"));

    private final JPanel pnlStart = new JPanel();
    private final StartButton btnStart  = new StartButton();

    private final DeckManager lstDecks = new DeckManager(GameType.Draft, CDeckEditorUI.SINGLETON_INSTANCE.getCDetailPicture());

    private final JRadioButton radSingle = new FRadioButton(localizer.getMessage("lblPlayAnOpponent"));
    private final JRadioButton radMultiple = new FRadioButton(localizer.getMessage("lblPlayMultipleOpponents"));
    private final JRadioButton radAll = new FRadioButton(localizer.getMessage("lblPlayAll7opponents"));

    private final JComboBox<String> cbOpponent = new JComboBox<>();

    private final JLabel lblInfo = new FLabel.Builder()
        .fontAlign(SwingConstants.LEFT).fontSize(16).fontStyle(Font.BOLD)
        .text(localizer.getMessage("lblBuildorselectadeck")).build(); // This creates the Build or Select a Deck at the top of the page, it is used in the constructor below

    private final FLabel lblDir1 = new FLabel.Builder()
        .text(localizer.getMessage("lblDraftText1"))
        .fontSize(12).build();

    private final FLabel lblDir2 = new FLabel.Builder()
        .text(localizer.getMessage("lblDraftText2"))
        .fontSize(12).build();

    private final FLabel lblDir3 = new FLabel.Builder()
        .text(localizer.getMessage("lblDraftText3"))
        .fontSize(12).build();

    private final FLabel btnBuildDeck = new FLabel.ButtonBuilder().text(localizer.getMessage("lblNewBoosterDraftGame")).fontSize(16).build(); // This is the New Booster Draft Game button

    /**
     * Constructor.
     */
    VSubmenuDraft() { // This constructor just sets up the values for the stuff at the bottom of the page underneath the table. Populate() actually draws everything
        btnStart.setEnabled(false);

        lblTitle.setBackground(FSkin.getColor(FSkin.Colors.CLR_THEME2));

        lstDecks.setCaption(localizer.getMessage("lblDraftDecks")); // This prints Draft Decks on the page

        final JXButtonPanel grpPanel = new JXButtonPanel();

        // Use wmin to specify the minimum width (200px), but allow it to grow if necessary
        // The original code was w 200px!, h 30px!
        grpPanel.add(radSingle, "wmin 200px, growx, h 30px!");
        grpPanel.add(radMultiple, "wmin 200px, growx, h 30px!");
        grpPanel.add(radAll, "wmin 200px, growx, h 30px!");
        radSingle.setSelected(true);
        grpPanel.add(cbOpponent, "wmin 200px, growx, h 30px!");

        pnlStart.setLayout(new MigLayout("insets 0, gap 0, wrap 2"));
        pnlStart.setOpaque(false);
        pnlStart.add(grpPanel, "gapright 20");
        pnlStart.add(btnStart);

    }

    /* (non-Javadoc)
     * @see forge.view.home.IViewSubmenu#getGroup()
     */
    @Override
    public EMenuGroup getGroupEnum() {
        return EMenuGroup.SANCTIONED;
    }

    /* (non-Javadoc)
     * @see forge.gui.home.IVSubmenu#getMenuTitle()
     */
    @Override
    public String getMenuTitle() {
        return localizer.getMessage("lblBoosterDraft");
    }

    /* (non-Javadoc)
     * @see forge.gui.home.IVSubmenu#getItemEnum()
     */
    @Override
    public EDocID getItemEnum() {
        return EDocID.HOME_DRAFT;
    }

    public FLabel getBtnBuildDeck() {
        return this.btnBuildDeck;
    }

    /** @return {@link javax.swing.JButton} */
    public JButton getBtnStart() {
        return this.btnStart;
    }

    /** @return {@link javax.swing.JRadioButton} */
    public boolean isSingleSelected() {
        return radSingle.isSelected();
    }
    public boolean isGauntlet() {
        return radAll.isSelected();
    }

    /** @return {@link forge.itemmanager.DeckManager} */
    public DeckManager getLstDecks() {
        return lstDecks;
    }

    public JComboBox<String> getCbOpponent() { return cbOpponent; }
    public JRadioButton getRadSingle() { return radSingle; }
    public JRadioButton getRadMultiple() { return radMultiple; }
    public JRadioButton getRadAll() { return radAll; }

    //========== Overridden from IVDoc

    /* (non-Javadoc)
     * @see forge.view.home.IViewSubmenu#populate()
     */
    @Override
    public void populate() { // This puts everything but the bottom menu on the page
        PnlDisplay pnlDisplay = VHomeUI.SINGLETON_INSTANCE.getPnlDisplay();
        pnlDisplay.removeAll();
        pnlDisplay.setLayout(new MigLayout("insets 0, gap 0, wrap, ax right"));

        pnlDisplay.add(lblTitle, "w 80%!, h 40px!, gap 0 0 50px 20px, ax right");
        pnlDisplay.add(lblInfo, "h 30px!, gap 0 0 0 0, ax center");
        //pnlDisplay.add(lblDir1, "gap 0 0 0 5px");
        //pnlDisplay.add(lblDir2, "gap 0 0 0 5px");
        //pnlDisplay.add(lblDir3, "gap 0 0 0 20px");

        // BOB - CODE INJECTION (Custom method)
        // --------------------
        // Original code to start a draft: pnlDisplay.add(btnBuildDeck, "w 250px!, h 30px!, ax center, gap 0 10% 0 20px"); // This will let you start a draft, controller manages the clicking
        // Create a container for the image buttons (so that they can be side by side)
        JPanel buttonPanel = new JPanel(new MigLayout("insets 0, gap 0px, flowx, ax center"));
        buttonPanel.setBackground(null);
        String[] buttonImages = {"Vintage", "Pauper", "Classic", "Custom"};
        for (String image : buttonImages) {
            JButton button = getImageButton(image);
            buttonPanel.add(button, "w 250px!, h 75px!, gap 10 10 0 0");
        }
        pnlDisplay.add(buttonPanel, "w 80%!, gap 0 0 20px 20px, pushx, growx, ax center"); // Add the button panel to the main display

        // Ensure gap between button panel and item manager container
        pnlDisplay.add(new ItemManagerContainer(lstDecks), "w 80%!, gap 0 0 0 0, pushy, growy, ax center"); // This will list the decks

        pnlDisplay.add(pnlStart, "gap 0px 0px 20px 50px, ax center"); // left, right, top, bottom

        pnlDisplay.repaint();
        pnlDisplay.revalidate();
    }

    private static JButton getImageButton(String draft_type) {
        final BufferedImage buttonImage;
        final BufferedImage buttonImageHover;
        final BufferedImage buttonImageClicked;

        try {
            // Load normal, hover, and clicked images
            buttonImage = ImageIO.read(new File(ForgeConstants.DEFAULT_SKINS_DIR + "fbut_" + draft_type + "_reg.jpg"));
            buttonImageHover = ImageIO.read(new File(ForgeConstants.DEFAULT_SKINS_DIR + "fbut_" + draft_type + "_hov.jpg"));
            buttonImageClicked = ImageIO.read(new File(ForgeConstants.DEFAULT_SKINS_DIR + "fbut_" + draft_type + "_clk.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Handle the case when the images cannot be loaded
        }

        // Create a JButton with the image
        JButton btnImage = new JButton();
        if (buttonImage != null) {
            btnImage.setIcon(new ImageIcon(buttonImage));  // Default icon
            btnImage.setToolTipText("Launch a " + draft_type + " draft");
        }
        btnImage.setFocusPainted(false);
        btnImage.setBorderPainted(false);
        btnImage.setContentAreaFilled(false); // Makes the button look cleaner without default background

        // MouseListener for hover and click state change
        btnImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (buttonImageHover != null) {
                    btnImage.setIcon(new ImageIcon(buttonImageHover));  // Change to hover icon
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (buttonImage != null) {
                    btnImage.setIcon(new ImageIcon(buttonImage));  // Revert to default icon when hover ends
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (buttonImageClicked != null) {
                    btnImage.setIcon(new ImageIcon(buttonImageClicked));  // Change to clicked icon
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (buttonImageHover != null) {
                    btnImage.setIcon(new ImageIcon(buttonImageHover));  // Change back to hover icon after release
                }
            }
        });

        // Add an ActionListener to the button for interaction
        btnImage.addActionListener(e -> System.out.println("Image button clicked!"));

        // Add the button to the UI layout
        return btnImage;
    }



    /* (non-Javadoc)
     * @see forge.gui.framework.IVDoc#getDocumentID()
     */
    @Override
    public EDocID getDocumentID() {
        return EDocID.HOME_DRAFT;
    }

    /* (non-Javadoc)
     * @see forge.gui.framework.IVDoc#getTabLabel()
     */
    @Override
    public DragTab getTabLabel() {
        return tab;
    }

    /* (non-Javadoc)
     * @see forge.gui.framework.IVDoc#setParentCell(forge.gui.framework.DragCell)
     */
    @Override
    public void setParentCell(final DragCell cell0) {
        this.parentCell = cell0;
    }

    /* (non-Javadoc)
     * @see forge.gui.framework.IVDoc#getParentCell()
     */
    @Override
    public DragCell getParentCell() {
        return parentCell;
    }

    /* (non-Javadoc)
     * @see forge.gui.framework.IVDoc#getLayoutControl()
     */
    @Override
    public CSubmenuDraft getLayoutControl() {
        return CSubmenuDraft.SINGLETON_INSTANCE;
    }
}
