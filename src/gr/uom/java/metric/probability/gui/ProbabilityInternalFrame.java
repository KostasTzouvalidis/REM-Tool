package gr.uom.java.metric.probability.gui;

import java.awt.Component;
import javax.swing.*;
import javax.swing.table.*;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gr.uom.java.metric.probability.xml.*;

public class ProbabilityInternalFrame extends JInternalFrame {
	
	static int openFrameCount = 0;
	static final int xOffset = 30, yOffset = 30;
	private SystemAxisObject system;
	private JTabbedPane tabbedPane;
	
	public ProbabilityInternalFrame(SystemAxisObject system) {
		super(system.getName(),true,true,true,true);
		this.system = system;
		tabbedPane = new JTabbedPane();
		
		JTable classProbTable = new JTable(new ProbabilityViewTableModel(system));
		classProbTable.getColumnModel().getColumn(0).setMinWidth(350);
		classProbTable.getColumnModel().getColumn(1).setMinWidth(100);
		classProbTable.setColumnSelectionAllowed(true);
		JScrollPane classProbScrollPane = new JScrollPane(classProbTable);
		
		//Antistoixo gia paketa
		JTable packageProbTable = new JTable(new ProbabilityViewPTableModel(system));
		packageProbTable.getColumnModel().getColumn(0).setMinWidth(350);
		packageProbTable.getColumnModel().getColumn(1).setMinWidth(100);
		packageProbTable.setColumnSelectionAllowed(true);
		JScrollPane packageProbScrollPane = new JScrollPane(packageProbTable);
		
		tabbedPane.addTab("Class probabilities", classProbScrollPane);
		tabbedPane.addTab("Package probabilities", packageProbScrollPane);

		String[] classColumnNames = new String[3];
		classColumnNames[0] = "Class";
		classColumnNames[1] = "Axis counter";
		classColumnNames[2] = "Receives ripple effect from";
		
		String[] packageColumnNames = new String[3];
		packageColumnNames[0] = "Package";
		packageColumnNames[1] = "Axis counter";
		packageColumnNames[2] = "Receives ripple effect from";
		
		//system.getClassNumber --- system.getPackageNumber
		DefaultTableModel defaultClassTableModel = new DefaultTableModel(classColumnNames, system.getClassNumber()) {
			public boolean isCellEditable(int row, int col) {
				if(col == 2)
					return true;
				else
					return false;
			}
		};
		
		DefaultTableModel defaultPackageTableModel = new DefaultTableModel(packageColumnNames, system.getPackageNumber()) {
			public boolean isCellEditable(int row, int col) {
				if(col == 2)
					return true;
				else
					return false;
			}
		};
		TableSorter classSorter = new TableSorter(defaultClassTableModel);
		
		JTable classAxisTable = new JTable(classSorter) {
			public TableCellRenderer getCellRenderer(int row, int column) {
				TableColumn tableColumn = getColumnModel().getColumn(column);
				TableCellRenderer renderer = tableColumn.getCellRenderer();
				if (renderer == null) {
					Class c = getColumnClass(column);
					if( c.equals(Object.class) ) {
						Object o = getValueAt(row,column);
						if( o != null )
							c = getValueAt(row,column).getClass();
					}
					renderer = getDefaultRenderer(c);
				}
				return renderer;
			}

			public TableCellEditor getCellEditor(int row, int column) {
				TableColumn tableColumn = getColumnModel().getColumn(column);
				TableCellEditor editor = tableColumn.getCellEditor();
				if (editor == null) {
					Class c = getColumnClass(column);
					if( c.equals(Object.class) ) {
						Object o = getValueAt(row,column);
						if( o != null )
							c = getValueAt(row,column).getClass();
					}
					editor = getDefaultEditor(c);
				}
				return editor;
			}
		};
		
		//Antistoixo Table gia ta paketa

		TableSorter packageSorter = new TableSorter(defaultPackageTableModel);
		
		JTable packageAxisTable = new JTable(packageSorter) {
			public TableCellRenderer getCellRenderer(int row, int column) {
				TableColumn tableColumn = getColumnModel().getColumn(column);
				TableCellRenderer renderer = tableColumn.getCellRenderer();
				if (renderer == null) {
					Class c = getColumnClass(column);
					if( c.equals(Object.class) ) {
						Object o = getValueAt(row,column);
						if( o != null )
							c = getValueAt(row,column).getClass();
					}
					renderer = getDefaultRenderer(c);
				}
				return renderer;
			}

			public TableCellEditor getCellEditor(int row, int column) {
				TableColumn tableColumn = getColumnModel().getColumn(column);
				TableCellEditor editor = tableColumn.getCellEditor();
				if (editor == null) {
					Class c = getColumnClass(column);
					if( c.equals(Object.class) ) {
						Object o = getValueAt(row,column);
						if( o != null )
							c = getValueAt(row,column).getClass();
					}
					editor = getDefaultEditor(c);
				}
				return editor;
			}
		};

        classSorter.setTableHeader(classAxisTable.getTableHeader());
		classAxisTable.setColumnSelectionAllowed(true);
		classAxisTable.getColumnModel().getColumn(0).setMinWidth(200);
		classAxisTable.getColumnModel().getColumn(1).setMinWidth(70);
		classAxisTable.getColumnModel().getColumn(1).setMaxWidth(80);
		classAxisTable.getColumnModel().getColumn(2).setMinWidth(200);
		classAxisTable.setDefaultRenderer(JComponent.class, new JComponentCellRenderer());
		classAxisTable.setDefaultEditor(JComponent.class, new JComponentCellEditor());
		
		packageSorter.setTableHeader(packageAxisTable.getTableHeader());
		packageAxisTable.setColumnSelectionAllowed(true);
		packageAxisTable.getColumnModel().getColumn(0).setMinWidth(200);
		packageAxisTable.getColumnModel().getColumn(1).setMinWidth(70);
		packageAxisTable.getColumnModel().getColumn(1).setMaxWidth(80);
		packageAxisTable.getColumnModel().getColumn(2).setMinWidth(200);
		packageAxisTable.setDefaultRenderer(JComponent.class, new JComponentCellRenderer());
		packageAxisTable.setDefaultEditor(JComponent.class, new JComponentCellEditor());
		
		ListIterator it = system.getClassListIterator();
		int counter = 0;
		
		while(it.hasNext()) {
			ClassAxisObject ca = (ClassAxisObject)it.next();
			classAxisTable.setValueAt(ca.getName(), counter, 0);
			List axisList = getAxisToClass(ca);
			classAxisTable.setValueAt(new Integer(axisList.size()), counter, 1);
			
			ListIterator axisListIterator = axisList.listIterator();
			JComboBox comboBox = new JComboBox();
			while(axisListIterator.hasNext()) {
				String axis = (String)axisListIterator.next();
				comboBox.addItem(axis);
			}
			classAxisTable.setValueAt(comboBox, counter, 2);
			counter ++;
		}
		
		Iterator<PackageAxisObject> pit = system.getPackageSetIterator();
		counter = 0;
		
		while(pit.hasNext())
		{
			PackageAxisObject pao = (PackageAxisObject)pit.next();
			packageAxisTable.setValueAt(pao.getName(), counter, 0);
			packageAxisTable.setValueAt(new Integer(pao.getNumberOfReferences()), counter, 1);
			
			List<PackageAxis> axisList = pao.getReferences();
			Iterator axisListIterator = axisList.iterator();
			JComboBox comboBox = new JComboBox();

			while(axisListIterator.hasNext())
			{
				PackageAxis axis = (PackageAxis)axisListIterator.next();
				comboBox.addItem(axis.getToPackage() + " - " + axis.getProbability());
			}
			packageAxisTable.setValueAt(comboBox, counter, 2);
			counter++;
		}
		
		
		JScrollPane classAxisScrollPane = new JScrollPane(classAxisTable);
		JScrollPane packageAxisScrollPane = new JScrollPane(packageAxisTable);
		tabbedPane.addTab("Class Axis browser", classAxisScrollPane);
		tabbedPane.addTab("Package Axis browser", packageAxisScrollPane);
		//table.setPreferredScrollableViewportSize(new Dimension(300, 100));
		this.setContentPane(tabbedPane);
		this.setVisible(true);
		this.setSize(600,500);
		this.setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
		openFrameCount++;
		//this.pack();
	}
	
	private List getAxisToClass(ClassAxisObject classAxisObject) {
		ListIterator classIt = system.getClassListIterator();
		List list = new ArrayList();
		
		while(classIt.hasNext()) {
			ClassAxisObject ca = (ClassAxisObject)classIt.next();
			ListIterator axisIt = ca.getAxisListIterator();
			
			while(axisIt.hasNext()) {
				Axis axis = (Axis)axisIt.next();
				if(axis.getFromClass().equals(classAxisObject.getName()) && !axis.getToClass().equals(axis.getFromClass()) ) {
					list.add(axis.getToClass() + " (propagation factor: " + axis.getProbability() + ")");
				} 
			}
		}
		
		return list;
	}
	
	class JComponentCellRenderer implements TableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
			return (JComponent)value;
		}
	}
}