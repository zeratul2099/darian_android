package me.zeratul.darian;

import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by zeratul on 19.06.15.
 */
public class MartianDate {


    static double SECONDS_A_DAY = 86400.0;
    static double E_DAYS_TIL_UNIX = 719527.0;
    static double EPOCH_OFFSET  = 587744.77817;
    static double MARS_TO_EARTH_DAYS = 1.027491251;

    public String[] SOL_NAMES = {"Sol Solis", "Sol Lunae", "Sol Martis", "Sol Mercurii", "Sol Jovis", "Sol Veneris", "Sol Saturni"};
    public String[] MONTH_NAMES = {"Sagittarius", "Dhanus", "Capricornus", "Makara", "Aquarius", "Kumbha", "Pisces", "Mina", "Aries", "Mesha", "Taurus", "Rishabha", "Gemini", "Mithuna", "Cancer", "Karka", "Leo", "Simha", "Virgo", "Kanya", "Libra", "Tula", "Scorpius", "Vrishika"};
    public double total_sols;
    public int year;
    public int sol_of_year;
    public int season;
    public int sol_of_season;
    public int month_of_season;
    public int month;
    public int sol;
    public int week_sol;
    public int hour;
    public int min;
    public int sec;
    public String week_sol_name;
    public String month_name;
    public String type;

    public void init_names(String type) {
        // sol names

        if (type.equals("martiana") || type.equals("hensel")) {
            this.SOL_NAMES = new String[]{"Sol Solis", "Sol Lunae", "Sol Martis", "Sol Mercurii", "Sol Jovis", "Sol Veneris", "Sol Saturni"};
        } else if (type.equals("defrost")) {
            this.SOL_NAMES = new String[]{"Axatisol", "Benasol", "Ciposol", "Domesol", "Erjasol", "Fulisol", "Gavisol"};
        } else if (type.equals("areosynchronous")) {
            this.SOL_NAMES = new String[]{"Heliosol", "Phobosol", "Deimosol", "Terrasol", "Venusol", "Mercurisol", "Jovisol"};
        }

        // month names
        if (type.equals("martiana")) {
            this.MONTH_NAMES = new String[]{"Sagittarius", "Dhanus", "Capricornus", "Makara", "Aquarius", "Kumbha", "Pisces", "Mina", "Aries", "Mesha", "Taurus", "Rishabha", "Gemini", "Mithuna", "Cancer", "Karka", "Leo", "Simha", "Virgo", "Kanya", "Libra", "Tula", "Scorpius", "Vrishika"};
        } else if (type.equals("defrost") || type.equals("areosynchronous")) {
            this.MONTH_NAMES = new String[]{"Adir", "Bora", "Coan", "Deti", "Edal", "Flo", "Geor", "Heliba", "Idanon", "Jowani", "Kireal", "Larno", "Medior", "Neturima", "Ozulikan", "Pasurabi", "Rudiakel", "Safundo", "Tiunor", "Ulasja", "Vadeun", "Wakumi", "Xetual", "Zungo"};
        } else if (type.equals("hensel")) {
            this.MONTH_NAMES = new String[]{"Vernalis", "Duvernalis", "Trivernalis", "Quadrivernalis", "Pentavernalis", "Hexavernalis", "Aestas", "Duestas", "Triestas", "Quadrestas", "Pentestas", "Hexestas", "Autumnus", "Duautumn", "Triautumn", "Quadrautumn", "Pentautumn", "Hexautumn", "Unember", "Duember", "Triember", "Quadrember", "Pentember", "Hexember"};
        }

    }

    public MartianDate(Calendar earth_date) {
        this.init_names("martiana");
        double seconds = (earth_date.getTime().getTime()) / 1000.0;
        this.init_by_epoch(seconds);

    }

    public MartianDate(Calendar earth_date, String type) {
        assert (type == "martiana" || type == "defrost" || type == "hensel" || type == "areosynchronous");
        this.init_names(type);
        double seconds = (earth_date.getTime().getTime()) / 1000.0;
        this.init_by_epoch(seconds);
    }

    public MartianDate(int year, int month, int day) {
        Calendar date = new GregorianCalendar(year, month, day);
        double seconds = (date.getTime().getTime()) / 1000.0;
        this.init_by_epoch(seconds);
    }


    public void init_by_epoch(double timestamp) {
        double days = ((timestamp / this.SECONDS_A_DAY) + this.E_DAYS_TIL_UNIX);
        this.total_sols = (days - this.EPOCH_OFFSET) / this.MARS_TO_EARTH_DAYS;
        this.init_time();
        this.year_and_sol_of_year();
        this.set_season_by_sol_of_year();
        this.sol_of_season = this.sol_of_year - 167 * this.season;
        this.month_of_season = (int)Math.floor(this.sol_of_season / 28);
        this.month = (this.month_of_season + (6 * this.season) + 1);
        this.sol = (this.sol_of_year - (((this.month - 1) * 28) - this.season) + 1);
        this.week_sol = (int)(((this.sol -1) % 7) + 1);
        this.set_week_sol_name_by_week_sol_and_type();
        this.set_month_name_by_month_and_type();

    }

    public void init_time() {
        double partial_sol = this.total_sols - Math.floor(this.total_sols);
        double hour = partial_sol * 24;
        double min = (hour - Math.floor(hour)) * 60;
        this.hour = (int)Math.floor(hour);
        this.min = (int)Math.floor(min);
        this.sec = (int)Math.floor((min - Math.floor(min)) * 60);
    }

    public void year_and_sol_of_year() {

        double sD = Math.floor(this.total_sols / 334296);
        double doD = Math.floor(this.total_sols - (sD * 334296));
        double sC = 0;
        double doC = doD;
        if (doD != 0) {
            sC = Math.floor((doD -1) / 66859);
        }
        if (sC != 0) {
            doC -= ((sC * 66859) + 1);
        }
        double sX = 0;
        double doX = doC;
        if (sC != 0) { //century that does not begin with leap day
            sX = Math.floor((doC + 1) / 6686);
            if (sX != 0) {
                doX -= ((sX * 6686) - 1);
            }
        } else {
            sX = Math.floor(doC / 6686);
            if (sX != 0) {
                doX -= (sX * 6686);
            }
        }
        double sII = 0;
        double doII = doX;
        if ((sC != 0) && (sX == 0)) { // decade that does not begin with leap day
            sII = Math.floor(doX / 1337);
            if (sII != 0) {
                doII -= (sII * 1337);
            }
        } else { // 1338, 1337, 1337, 1337 ...
            if (doX != 0) {
                sII = Math.floor((doX - 1) / 1337);
            }
            if (sII != 0) {
                doII -= ((sII * 1337) + 1);
            }
        }

        double sI = 0;
        double doI = doII;
        if ((sII == 0) && ((sX != 0) || ((sX == 0) && (sC == 0)))) {
            sI = Math.floor(doII / 669);
            if (sI != 0) {
                doI -= 669;
            }
        } else { // 668, 669
            sI = Math.floor((doII + 1) / 669);
            if (sI != 0) {
                doI -= 668;
            }
        }
        this.year = (int)((500 * sD) + (100 * sC) + (10 * sX) + (2 * sII) + sI);
        this.sol_of_year = (int)doI;

    }
    public void set_season_by_sol_of_year() {
        if (this.sol_of_year < 167) {
            this.season = 0;
        }  else if (this.sol_of_year < 334) {
            this.season = 1;
        } else if (this.sol_of_year < 501) {
            this.season = 2;
        } else {
            this.season = 3;
        }
    }

    public void set_week_sol_name_by_week_sol_and_type() {
        this.week_sol_name = this.SOL_NAMES[this.week_sol-1];
    }

    public void set_month_name_by_month_and_type() {
        this.month_name = this.MONTH_NAMES[this.month-1];
    }

}
