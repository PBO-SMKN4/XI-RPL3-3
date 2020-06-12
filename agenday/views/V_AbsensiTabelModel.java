/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agenday.views;

import agenday.Absen;
import agenday.WargaSekolah;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author YatiaNurdani
 */
public class V_AbsensiTabelModel extends AbstractTableModel{
    public ArrayList<WargaSekolah> list;
    public ArrayList<Absen> absen;
    
    public V_AbsensiTabelModel(){
        list = new ArrayList<WargaSekolah>();
        absen = new ArrayList<Absen>();
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0:
                return rowIndex + 1;
            case 1:
                return list.get(rowIndex).getNIS();
            case 2:
                return list.get(rowIndex).getNama();
            case 3:
                return list.get(rowIndex).getJenisKelamin();
            case 4:
                return list.get(rowIndex).getSakit();
            case 5:
                return list.get(rowIndex).getIzin();
            case 6:
                return list.get(rowIndex).getAlpha();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0:
                return "NO.";
            case 1:
                return "NIS";
            case 2:
                return "Nama";
            case 3:
                return "JK";
            case 4:
                return "Sakit";
            case 5:
                return "Izin";
            case 6:
                return "Alpha";
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(columnIndex == 4){
            if(list.get(rowIndex).getSakit()== true){
                absen.get(rowIndex).setSakit(0);
                absen.get(rowIndex).Kehadiran(false, "Hadir");
            }
            else{
                absen.get(rowIndex).setSakit(1);
                absen.get(rowIndex).Kehadiran(true, "Sakit");
            }
            return true;
        }
        else if(columnIndex == 5){
            if(list.get(rowIndex).getIzin()== true){
                absen.get(rowIndex).setIzin(0);
                absen.get(rowIndex).Kehadiran(false, "Hadir");
            }
            else{
                absen.get(rowIndex).setIzin(1);
                absen.get(rowIndex).Kehadiran(true, "Izin");
            }
            return true;
        }
        else if(columnIndex == 6){
            if(list.get(rowIndex).getAlpha()== true){
                absen.get(rowIndex).setAlfa(0);
                absen.get(rowIndex).Kehadiran(false, "Hadir");
            }
            else{
                absen.get(rowIndex).setAlfa(1);
                absen.get(rowIndex).Kehadiran(true, "Alpha");
            }
            return true;
        }
        else{
            absen.get(rowIndex).Kehadiran(false, "Hadir");
            return false;
        }
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        if(columnIndex == 4){
            return Boolean.class;
        }
        else if(columnIndex == 5){
            return Boolean.class;
        }
        else if(columnIndex == 6){
            return Boolean.class;
        }
        else{
            return super.getColumnClass(columnIndex);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);
        if(aValue != null && aValue instanceof Boolean && columnIndex == 4){
            boolean select = (Boolean) aValue;
            list.get(rowIndex).setSakit(select);
        }
        else if(aValue != null && aValue instanceof Boolean && columnIndex == 5){
            boolean select = (Boolean) aValue;
            list.get(rowIndex).setIzin(select);
        }
        else if(aValue != null && aValue instanceof Boolean && columnIndex == 6){
            boolean select = (Boolean) aValue;
            list.get(rowIndex).setAlpha(select);
        }
    }
    
    public void add(WargaSekolah siswa, Absen absensi){
        list.add(siswa);
        absen.add(absensi);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
    
}
