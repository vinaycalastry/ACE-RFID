
using System;
using System.Collections.Generic;
using System.Linq;
using System.Xml.Linq;



public class MatDB
{
    private const string xmlPath = "filaments.xml";

    public static void AddFilament(Filament filament)
    {
        try
        {
            XDocument doc = XDocument.Load(xmlPath);

            int position = 0;

            var pos = doc.Root.Elements("Filament")
                        .Select(f => (int)f.Element("Position"))
                        .OrderBy(p => p)
                        .ToList();
            if (pos.Any())
            {
                for (int i = 0; i <= pos.Last(); i++)
                {
                    if (!pos.Contains(i))
                    {
                        position = i;
                        goto foundPos;
                    }
                }
                position = pos.Last() + 1;
            }
        foundPos:
            doc.Root.Add(
                new XElement("Filament",
                new XElement("Position", position),
                new XElement("FilamentId", filament.FilamentId),
                new XElement("FilamentName", filament.FilamentName),
                new XElement("FilamentVendor", filament.FilamentVendor),
                new XElement("FilamentParam", filament.FilamentParam)
                )
            );
            doc.Save(xmlPath);
        }
        catch (Exception)
        {}
    }


    public static void DeleteFilament(Filament filament)
    {
        try
        {
            XDocument doc = XDocument.Load(xmlPath);
            doc.Root.Elements("Filament")
                .Where(f => (int)f
                .Element("Position") == filament.Position).Remove();
            doc.Save(xmlPath);
        }
        catch (Exception)
        {}
    }


    public static void UpdateFilament(Filament filament)
    {
        try
        {
            XDocument doc = XDocument.Load(xmlPath);
            XElement fil = doc.Root.Elements("Filament")
                                            .FirstOrDefault(f => (int)f
                                            .Element("Position") == filament.Position);
            if (fil != null)
            {
                fil.Element("FilamentId").Value = filament.FilamentId;
                fil.Element("FilamentVendor").Value = filament.FilamentVendor;
                fil.Element("FilamentName").Value = filament.FilamentName;
                fil.Element("FilamentParam").Value = filament.FilamentParam;
                doc.Save(xmlPath);
            }
        }
        catch (Exception)
        {}
    }


    public static int GetItemCount()
    {
        try
        {
            XDocument doc = XDocument.Load(xmlPath);
            return doc.Root.Elements("Filament").Count();
        }
        catch (Exception)
        {
            return 0;
        }
    }


    public static Filament GetFilamentById(string filamentId)
    {
        try
        {
            XDocument doc = XDocument.Load(xmlPath);
            return doc.Root.Elements("Filament")
                      .Where(f => (string)f.Element("FilamentId") == filamentId)
                      .Select(f => new Filament
                      {
                          Position = (int)f.Element("Position"),
                          FilamentId = (string)f.Element("FilamentId"),
                          FilamentName = (string)f.Element("FilamentName"),
                          FilamentVendor = (string)f.Element("FilamentVendor"),
                          FilamentParam = (string)f.Element("FilamentParam")
                      })
                      .FirstOrDefault();
        }
        catch (Exception)
        {
            return null;
        }
    }


    public static Filament GetFilamentByName(string filamentName)
    {
        try
        {
            XDocument doc = XDocument.Load(xmlPath);
            return doc.Root.Elements("Filament")
                      .Where(f => (string)f.Element("FilamentName") == filamentName)
                      .Select(f => new Filament
                      {
                          Position = (int)f.Element("Position"),
                          FilamentId = (string)f.Element("FilamentId"),
                          FilamentName = (string)f.Element("FilamentName"),
                          FilamentVendor = (string)f.Element("FilamentVendor"),
                          FilamentParam = (string)f.Element("FilamentParam")
                      })
                      .FirstOrDefault();
        }
        catch (Exception)
        {
            return null;
        }
    }


    public static List<Filament> GetFilamentsByVendor(string filamentVendor)
    {
        try
        {
            XDocument doc = XDocument.Load(xmlPath);
            return doc.Root.Elements("Filament")
                      .Where(f => (string)f.Element("FilamentVendor") == filamentVendor)
                      .Select(f => new Filament
                      {
                          Position = (int)f.Element("Position"),
                          FilamentId = (string)f.Element("FilamentId"),
                          FilamentName = (string)f.Element("FilamentName"),
                          FilamentVendor = (string)f.Element("FilamentVendor"),
                          FilamentParam = (string)f.Element("FilamentParam")
                      })
                      .ToList();
        }
        catch (Exception)
        {
            return new List<Filament>();
        }
    }


    public static List<Filament> GetAllFilaments()
    {
        try
        {
            XDocument doc = XDocument.Load(xmlPath);
            return doc.Root.Elements("Filament")
                      .OrderBy(f => (int)f.Element("Position"))
                      .Select(f => new Filament
                      {
                          Position = (int)f.Element("Position"),
                          FilamentId = (string)f.Element("FilamentId"),
                          FilamentName = (string)f.Element("FilamentName"),
                          FilamentVendor = (string)f.Element("FilamentVendor"),
                          FilamentParam = (string)f.Element("FilamentParam")
                      })
                      .ToList();
        }
        catch (Exception)
        {
            return new List<Filament>();
        }
    }


    public static void PopulateDatabase()
    {
        try
        {
            if (!System.IO.File.Exists(xmlPath))
            {
                XDocument doc = new XDocument(new XElement("Filaments"));

                doc.Root.Add(new XElement("Filament",
                new XElement("Position", 0),
                new XElement("FilamentId", "SHABBK-102"),
                new XElement("FilamentName", "ABS"),
                new XElement("FilamentVendor", "AC"),
                new XElement("FilamentParam", "220|250|90|100")));


                doc.Root.Add(new XElement("Filament",
                new XElement("Position", 1),
                new XElement("FilamentId", ""),
                new XElement("FilamentName", "ASA"),
                new XElement("FilamentVendor", ""),
                new XElement("FilamentParam", "240|280|90|100")));



                doc.Root.Add(new XElement("Filament",
                new XElement("Position", 2),
                new XElement("FilamentId", ""),
                new XElement("FilamentName", "PETG"),
                new XElement("FilamentVendor", ""),
                new XElement("FilamentParam", "230|250|70|90")));



                doc.Root.Add(new XElement("Filament",
                new XElement("Position", 3),
                new XElement("FilamentId", "AHPLBK-101"),
                new XElement("FilamentName", "PLA"),
                new XElement("FilamentVendor", "AC"),
                new XElement("FilamentParam", "190|230|50|60")));



                doc.Root.Add(new XElement("Filament",
                new XElement("Position", 4),
                new XElement("FilamentId", "AHPLPBK-102"),
                new XElement("FilamentName", "PLA+"),
                new XElement("FilamentVendor", "AC"),
                new XElement("FilamentParam", "210|230|45|60")));



                doc.Root.Add(new XElement("Filament",
                new XElement("Position", 5),
                new XElement("FilamentId", ""),
                new XElement("FilamentName", "PLA Glow"),
                new XElement("FilamentVendor", ""),
                new XElement("FilamentParam", "190|230|50|60")));



                doc.Root.Add(new XElement("Filament",
                new XElement("Position", 6),
                new XElement("FilamentId", "AHHSBK-103"),
                new XElement("FilamentName", "PLA High Speed"),
                new XElement("FilamentVendor", "AC"),
                new XElement("FilamentParam", "190|230|50|60")));



                doc.Root.Add(new XElement("Filament",
                new XElement("Position", 7),
                new XElement("FilamentId", ""),
                new XElement("FilamentName", "PLA Marble"),
                new XElement("FilamentVendor", ""),
                new XElement("FilamentParam", "200|230|50|60")));



                doc.Root.Add(new XElement("Filament",
                new XElement("Position", 8),
                new XElement("FilamentId", "HYGBK-102"),
                new XElement("FilamentName", "PLA Matte"),
                new XElement("FilamentVendor", "AC"),
                new XElement("FilamentParam", "190|230|55|65")));



                doc.Root.Add(new XElement("Filament",
                new XElement("Position", 9),
                new XElement("FilamentId", ""),
                new XElement("FilamentName", "PLA SE"),
                new XElement("FilamentVendor", ""),
                new XElement("FilamentParam", "190|230|55|65")));



                doc.Root.Add(new XElement("Filament",
                new XElement("Position", 10),
                new XElement("FilamentId", "AHSCWH-102"),
                new XElement("FilamentName", "PLA Silk"),
                new XElement("FilamentVendor", "AC"),
                new XElement("FilamentParam", "200|230|55|65")));



                doc.Root.Add(new XElement("Filament",
                new XElement("Position", 11),
                new XElement("FilamentId", "STPBK-101"),
                new XElement("FilamentName", "TPU"),
                new XElement("FilamentVendor", "AC"),
                new XElement("FilamentParam", "210|230|25|60")));

                doc.Save(xmlPath);
            }
        }
        catch (Exception) { }
    }

}






