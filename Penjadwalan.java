import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Scanner;
import java.lang.Math;

public class Penjadwalan
{
    public static void main(String[] args) throws IOException
    {
        String nama[][] = {{"car-f-92", "Carleton92"}, {"car-s-91", "Carleton91"}, {"ear-f-83", "EarlHaig83"}, {"hec-s-92", "EdHEC92"},
                {"kfu-s-93", "KingFahd93"}, {"lse-f-91", "LSE91"}, {"pur-s-93", "pur-s-93"}, {"rye-s-93", "rye-s-93"}, {"sta-f-83", "St.Andrews83"},
                {"tre-s-92", "Trent92"},	{"uta-s-92", "TorontoAS92"}, {"ute-s-92", "TorontoE92"}, {"yor-f-83", "YorkMills83"}};
        int jts[] = {32, 35, 24, 18, 20, 18, 42, 23, 13, 23, 35, 10, 21};
        for(int i=0; i<nama.length;i++)
            System.out.println(i+1 + "  "+ nama[i][0]);
        Scanner in = new Scanner(System.in);
        System.out.print("Nomor data yang akan dijadwalankan : ");
        int pilihan = in.nextInt();
        String ex = "";
        String out = "";
        int jj = -1;
        for(int i=0; i<nama.length; i++)
        {
            if(pilihan==i+1)
            {
                ex = nama[i][0];
                out = nama[i][1];
                jj = jts[i];
            }
        }
        String file = "E:\\AELISA NAILIN\\Documents\\KULIAH\\Semester 7\\OKH\\Ngoding\\Toronto\\" + ex;
        BufferedReader reader = new BufferedReader(new FileReader(file + ".crs"));
        int exam = 0;
        while (reader.readLine() != null) exam++;
        reader.close();
        BufferedReader pembaca = new BufferedReader(new FileReader(file + ".stu"));
        int siswa = 0;
        while (pembaca.readLine() != null) siswa++;
        pembaca.close();

        int data[][] = new int[exam][exam];
        int sort [][] = new int[exam][2];
        int timeslot [][] = new int[exam][2];
        int ts = 1;
        int count = 0;
        int max [][] = new int [1][2];
        max[0][0] = -1;
        max[0][1] = -1;
        int x = 0;
        for (int i=0; i<data.length; i++)
            for(int j=0; j<data.length; j++)
                data[i][j] = 0;
        int degree[][] = new int [exam][2];
        for (int i=0; i<degree.length; i++)
            for (int j=0; j<degree[0].length; j++)
                degree[i][0] = i+1;
        //long awal = System.nanoTime();
        try {

            File f = new File(file+".stu");

            BufferedReader b = new BufferedReader(new FileReader(f));

            String readLine = "";

            while ((readLine = b.readLine()) != null) {
                //System.out.println(readLine);
                String tmp [] = readLine.split(" ");
                for(int i=0; i<tmp.length; i++)
                    for(int j=0; j<tmp.length; j++)
                        if(tmp[i] != tmp[j])
                            data[Integer.parseInt(tmp[i])-1][Integer.parseInt(tmp[j])-1]++;
            }


            for (int i=0; i<exam; i++)
            {
                for (int j=0; j<exam; j++)
                    if(data[i][j] > 0)
                        count++;
                    else
                        count = count;
                degree[i][1] = count;
                count=0;
            }

            for(int a=0; a<degree.length; a++)
            {
                for(int i=0; i<degree.length; i++)
                {
                    if(max[0][1]<degree[i][1])
                    {
                        max[0][0] = degree[i][0];
                        max[0][1] = degree[i][1];
                        x = i;
                    }
                }
                degree[x][0] = -2;
                degree[x][1] = -2;
                sort[a][0] = max[0][0];
                sort[a][1] = max[0][1];
                max[0][0] = -1;
                max[0][1] = -1;
            }

            for(int i=0; i<timeslot.length; i++)
            {
                for(int j=0; j<timeslot[i].length; j++)
                {
                    timeslot[i][0] = i+1;
                    timeslot[i][1] = -1;
                }
            }
            int [][] jadual = new int[exam][2];
            int [][] sat = new int[exam][2];
            for(int i=0; i<timeslot.length; i++)
            {
                for(int j=0; j<timeslot[i].length; j++)
                {
                    jadual[i][0] = i+1;
                    jadual[i][1] = -1;
                }
            }

            for(int i=0; i<jadual.length; i++)
            {
                int ujian = calculate(sat, jj);
                for(int j=0; j<exam; j++)
                {
                    if(isOk(ujian, j, data, jadual))
                    {
                        jadual[ujian][1] = j;
                        for(int k=0; k<data.length; k++)
                            if(data[ujian][k]!=0 && jadual[k][1]==-1)
                                sat[k][1] = sat[k][1]-1;
                        break;
                    }
                }
            }

            Jadwal aaa = new Jadwal(jadual, data, siswa, out, jj);
            ///System.out.println(aaa.getTimeslot());
            //aaa.repair();

            for(int i=0; i<sort.length; i++)
            {
                for (int j=0; j<=ts; j++)
                {
                    if(isSafe(i, j, data, sort, timeslot))
                    {
                        timeslot[sort[i][0]-1][1] = j;
                        break;
                    }
                    else{
                        ts++;
                    }
                }
            }

            //System.out.println(countPenalty(timeslot, data, siswa));
            //long selesai = System.nanoTime();

		/*for(int i=0; i<timeslot.length; i++) //print timeslot
			{
				for(int j=0; j<timeslot[i].length; j++)
				{
					System.out.print(jadual[i][j] + " ");
				}
				System.out.println();
		}*/

            //TextFileWritingExample1(timeslot, out);

            //System.out.println(countPenalty(timeslot, data, siswa));
            int tt = 0;
            for(int i=0; i<timeslot.length; i++)
            {
                if(timeslot[i][1]>tt)
                {
                    tt = timeslot[i][1];
                }
            }
            tt = tt+1;



            //long akhir  = selesai-awal;
            //System.out.println("Lama Running Time = "+(double)akhir/1000000000);
            long awal = System.nanoTime();
            //Jadwal rand = new Jadwal(timeslot, data, siswa);
            Jadwal fix = new Jadwal(timeslot, data, siswa, out, jj);
            //a.print();
            //hillClimbing(timeslot, data, siswa, tt, out, exam);
            //System.out.println(a.countPenalty());
            //a.cek();
            //HC(fix, rand);
            //fix.Simulated();
            //System.out.println(data.length);
            //fix.repair();
            //System.out.println(fix.contains());
            //System.out.println(fix.getTimeslot());
            //fix.TextFileWritingExample1();
            //back(0, 0, data, jadual, jj);

            //saturation
            //aaa.LAHC();

            //largest degree
            fix.HC();

            long selesai = System.nanoTime();
            long akhir  = selesai-awal;
            //System.out.println(a.safeAll());
            System.out.println("Lama Running Time = "+(double)akhir/1000000000);
            //System.out.print(tt);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void back(int ex, int jad, int[][]cm, int [][]jadual, int limit)
    {
        if(ex>=0 && ex<jadual.length && jad>=0 && jad<limit)
        {
            if(isOk(ex, jad, cm, jadual))
            {
                jadual[ex][1] = jad;
                back(ex+1, 0, cm, jadual, limit);
            }
            else
            {
                if(jad==limit-1)
                {
                    for(int i=ex; i<jadual.length; i++)
                        jadual[i][1] = -1;
                    int jd = jadual[ex-1][1]+1;
                    back(ex-1, jd, cm, jadual, limit);
                }
                else
                {
                    back(ex, jad+1, cm, jadual, limit);
                }
            }
        }
        else
        {
            System.out.println(limit);
            //Jadwal x = new Jadwal(jadual, cm, 1000, "xx", limit);
            //x.print();
        }
    }

    public static boolean isOk(int ex, int jad, int[][]cm, int [][]jadual)
    {
        for(int i=0; i<cm.length; i++)
            if(cm[ex][i]!=0 && jadual[i][1]==jad)
                return false;
            return true;
    }

    public static int calculate(int[][]sat, int batas)
    {
        int min = batas+1;
        int index = 0;
        for(int i=0; i<sat.length; i++) {
            if (sat[i][1] < min) {
                min = sat[i][1];
                index = i;
            }
        }
        return index;
    }

    public static void HC(Jadwal fix, Jadwal rand)
    {
        double S = fix.countPenalty();
        for(int i=0; i<20; i++)
        {
            rand.move(1);
            if(rand.safeAll())
            {
                if(rand.countPenalty()<S)
                {
                    fix = rand;
                    S = fix.countPenalty();
                }
                else
                {
                    rand = fix;
                }
            }
            else
            {
                rand = fix;
            }
            System.out.println(S);
        }
    }

    public static void hillClimbing(int jadwal[][], int matrix[][], int jumlah, int tt, String nama, int crs)
    {
        int waktu[][] = new int[jadwal.length][2];
        int waktu2[][] = new int[jadwal.length][2];
        for(int i=0; i<jadwal.length; i++)
            for(int j=0; j<jadwal[i].length; j++)
            {
                waktu[i][j] = jadwal[i][j];
                waktu2[i][j] = jadwal[i][j];
            }
        double s = countPenalty(waktu, matrix, jumlah);
        double d = s;
        for(int i=0; i<100000; i++)
        {
            int exr = (int) (Math.random()*(crs-1));
            int ttr = (int) (Math.random()*(tt-1));
            if(isSafeRand(exr, ttr, matrix, waktu2))
            {
                waktu2[exr][1] = ttr;
                double c = countPenalty(waktu2, matrix, jumlah);
                if(c<=s)
                {
                    s = countPenalty(waktu2, matrix, jumlah);
                    waktu[exr][1] = waktu2[exr][1];
                }
                else
                {
                    waktu2[exr][1] = waktu[exr][1];
                }
            }
            System.out.println("Iterasi ke "+ (i+1) +" penaltinya = " + countPenalty(waktu2, matrix, jumlah));
        }
        System.out.println("Penalti Solusi Awal = "+d);
        System.out.println("Solusi terbaik adalah = " + s);
        TextFileWritingExample1(waktu, nama);
    }

    public static boolean isSafeRand(int exr, int ttr, int matriks[][], int jd[][])
    {
        for(int i=0; i<matriks.length; i++)
            if(matriks[exr][i]!=0 && jd[i][1]==ttr)
                return false;
        return true;
    }

    public static double countPenalty(int jadwal[][], int matrix[][], int jumlah)
    {
        double penalty = 0;
        for(int i=0; i<matrix.length-1; i++)
        {
            for(int j=i+1; j<matrix.length; j++)
            {
                if(matrix[i][j]!=0)
                {
                    if(Math.abs(jadwal[j][1]-jadwal[i][1])>=1 && Math.abs(jadwal[j][1]-jadwal[i][1])<=5)
                    {
                        penalty = penalty + (matrix[i][j]*(Math.pow(2, (5-(Math.abs(jadwal[j][1]-jadwal[i][1]))))));
                    }
                }
            }
        }
        return penalty/jumlah;
    }

    public static boolean isSafe(int index, int m, int dat[][], int[][]srt, int[][]jadwal)
    {
        for(int i=0; i<srt.length; i++)
            if(dat[srt[index][0]-1][i]!=0 && jadwal[i][1] == m)
                return false;
        return true;
    }

    public static void TextFileWritingExample1(int[][]jadwal, String namaFile) {

        try {
            FileWriter writer = new FileWriter(namaFile+".sol", true);
            for (int i = 0; i <jadwal.length; i++) {
                for (int j = 0; j <jadwal[i].length; j++) {
                    writer.write(jadwal[i][j]+ " ");
                }
                //this is the code that you change, this will make a new line between each y value in the array
                writer.write("\n");   // write new line
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}