using System;
using System.Diagnostics;
using System.IO;
using System.Text.RegularExpressions;

namespace FlightContestManager
{
    class Program
    {
        static void Main(string[] args)
        {
            if (args.Length < 1) { return; }

            string scriptPath = args[0];

            if (!File.Exists(scriptPath)) { return; }
            
            string extension = Path.GetExtension(scriptPath).ToLower();

            string cmdArgs = Environment.CommandLine;

            // replace all spaces, within quoted substrings, to xFF
            cmdArgs = Regex.Replace(cmdArgs, "\"([^\"]*)\"", m =>
            {
                return "\"" + m.Groups[1].Value.Replace(" ", "\u00FF") + "\"";
            });

            // ensure there is only a single space between arguments
            cmdArgs = Regex.Replace(cmdArgs, "\\s+", " ");

            // split off parameters to be passed through to the script
            string[] parts = cmdArgs.Split(new char[] { ' ' }, 3);
            cmdArgs = "";
            if (parts.Length > 2)
            {
                // restore xFF to space
                cmdArgs = parts[2].Replace("\u00FF", " ");
            }

            ProcessStartInfo psi = new ProcessStartInfo();
            psi.FileName = "cmd.exe";
            psi.Arguments = "/c \"" + scriptPath + "\" " + cmdArgs;
            //System.Console.WriteLine("Run " + psi.FileName + " " + psi.Arguments);
            psi.RedirectStandardOutput = false;
            psi.RedirectStandardError = false;
            psi.UseShellExecute = false;
            psi.CreateNoWindow = true;

            using (Process process = new Process())
            {
                process.StartInfo = psi;
                process.Start();
            }
        }
    }
}
